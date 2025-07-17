package com.security.secure_vault.controller;

import com.security.secure_vault.model.FileDocument;
import com.security.secure_vault.service.FileService;
import com.security.secure_vault.service.SharedLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final FileService service;
    private final SharedLinkService sharedLinkService; // 🔹 Add this line

    @PostMapping
    public ResponseEntity<FileDocument> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.uploadFile(file));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FileDocument> update(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(service.updateFile(id, file));
    }

    @GetMapping
    public ResponseEntity<List<FileDocument>> getAll() {
        return ResponseEntity.ok(service.getAllFiles());
    }

    @GetMapping("/search")
    public ResponseEntity<List<FileDocument>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchFiles(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileDocument> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getFileMetadata(id));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws IOException {
        FileDocument file = service.getFileMetadata(id);
        byte[] data = service.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"")
                .header("Content-Type", file.getFileType())
                .body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    // 🔥 NEW: Generate share link
    @PostMapping("/share/{fileId}")
    public ResponseEntity<String> generateShareLink(@PathVariable Long fileId) {
        String link = sharedLinkService.generateLink(fileId);
        return ResponseEntity.ok(link); // You can copy this link and share anywhere (email, WhatsApp)
    }

    // 🔥 NEW: Access shared file via token (link)
    @GetMapping("/share/{token}")
    public ResponseEntity<byte[]> accessSharedFile(@PathVariable String token) throws IOException {
        byte[] data = sharedLinkService.getFileFromToken(token);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"shared_file\"")
                .header("Content-Type", "application/octet-stream")
                .body(data);
    }
}
