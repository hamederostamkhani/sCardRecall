package ir.hamedrostamkhani.backend.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStorageService {
    String store(MultipartFile file, String folder);
    String update(String oldFilePath, MultipartFile newFile, String folder);
    Resource load(String filePath);
    List<Resource> loadMultipleFiles(List<String> filePaths);
    void delete(String filePath);
}
