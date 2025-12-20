package ir.hamedrostamkhani.backend.controller;

import ir.hamedrostamkhani.backend.dto.request.FileBatchRequest;
import ir.hamedrostamkhani.backend.service.FileStorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService storageService;

    @GetMapping
    public ResponseEntity<Resource> download(@RequestParam String path) {
        // normalize & validate the path
        Resource resource = storageService.load(path);

        String filename = Paths.get(path).getFileName().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    /**
     * Batch load files
     */
    @PostMapping(value = "/batch", produces = MediaType.MULTIPART_MIXED_VALUE)
    public ResponseEntity<List<Resource>> loadMultipleFiles(@RequestBody @Valid FileBatchRequest request) {
        List<Resource> resources = storageService.loadMultipleFiles(request.paths());

        return ResponseEntity.ok()
                .contentType(MediaType.MULTIPART_MIXED)
                .body(resources);
    }
}
