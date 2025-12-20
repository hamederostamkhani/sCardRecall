package ir.hamedrostamkhani.backend.service;

import ir.hamedrostamkhani.backend.exception.file.*;
import ir.hamedrostamkhani.backend.util.StorageProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageProperties properties;
    private Path basePath;

    @PostConstruct
    void init() {
        try {
            basePath = properties.getPath().toAbsolutePath().normalize();
            Files.createDirectories(basePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize storage directory", e);
        }
    }

    /* ===================== STORE ===================== */

    public String store(MultipartFile file, String folder) {
        validateFile(file);

        String filename = generateFilename(file.getOriginalFilename());
        Path targetDir = resolveFolder(folder);
        Path targetFile = targetDir.resolve(filename).normalize();

        validatePath(targetFile);

        try {
            Files.createDirectories(targetDir);
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file", e);
        }

        return basePath.relativize(targetFile).toString();
    }

    /* ===================== UPDATE ===================== */

    public String update(String oldRelativePath, MultipartFile newFile, String folder) {
        validateFile(newFile);

        if (oldRelativePath != null) {
            Path oldPath = resolveRelative(oldRelativePath);

            try {
                if (Files.exists(oldPath) && filesAreEqual(oldPath, newFile)) {
                    return oldRelativePath; // The new file == The old file
                }
            } catch (Exception e) {
                throw new FileStorageException("Failed to compare files", e);
            }

            delete(oldRelativePath);
        }

        return store(newFile, folder);
    }

    /* ===================== DELETE ===================== */

    public void delete(String relativePath) {
        if (relativePath == null) return;

        Path path = resolveRelative(relativePath);

        try {
            if (Files.exists(path))
                Files.delete(path);
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file", e);
        }
    }

    /* ===================== LOAD ===================== */

    public Resource load(String relativePath) {
        Path path = resolveRelative(relativePath);

        try {
            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException("File not found or unreadable");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new FileStorageException("Invalid file path", e);
        }
    }

    public List<Resource> loadMultipleFiles(List<String> filePaths) {
        List<Resource> resources = new ArrayList<>();
        for (String path : filePaths) {
            resources.add(load(path));
        }
        return resources;
    }

    /* ===================== SECURITY ===================== */

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new EmptyFileException("File cannot be empty");
        }

        if (file.getSize() > properties.getMaxSize()) {
            throw new FileSizeException("File size exceeds limit. File size should be less than " + properties.getMaxSize() + " Byte");
        }

        if (!properties.getAllowedTypes().contains(file.getContentType())) {
            throw new FileTypeException("File type is not allowed");
        }
    }

    private Path resolveFolder(String folder) {
        Path resolved = basePath.resolve(folder).normalize();
        validatePath(resolved);
        return resolved;
    }

    private Path resolveRelative(String relativePath) {
        Path resolved = basePath.resolve(relativePath).normalize();
        validatePath(resolved);
        return resolved;
    }

    /**
     * Check Path Traversal attacks
     */
    private void validatePath(Path path) {
        if (!path.startsWith(basePath)) {
            throw new InvalidFilePathException("Invalid path (possible path traversal attack)");
        }
    }

    private String generateFilename(String originalName) {
        return UUID.randomUUID() + "_" + originalName;
    }

    private boolean filesAreEqual(Path oldFile, MultipartFile newFile) throws Exception {
        byte[] oldHash = sha256(Files.readAllBytes(oldFile));
        byte[] newHash = sha256(newFile.getBytes());
        return MessageDigest.isEqual(oldHash, newHash);
    }

    private byte[] sha256(byte[] data) throws Exception {
        return MessageDigest.getInstance("SHA-256").digest(data);
    }
}
