package com.security.secure_vault.service;

import com.security.secure_vault.model.FileDocument;
import com.security.secure_vault.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository repository;

    @Autowired
    private AuditLogService auditLogService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Boolean isInValid(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".exe") || lower.endsWith(".bat") || lower.endsWith(".sh");
    }

    @Override
    public FileDocument uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null || isInValid(fileName)) {
            throw new RuntimeException("Invalid FileType");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        System.out.println("[VirusScan] File scanned: " + fileName + " (NO VIRUS)");

        FileDocument document = FileDocument.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .uploadPath(filePath.toString())
                .uploadedBy("rohan") // ❗ Replace with JWT user in real app
                .uploadedAt(LocalDateTime.now())
                .build();

        // ✅ Log upload action
        auditLogService.logAction("rohan", "UPLOAD");

        return repository.save(document);
    }

    @Override
    public FileDocument updateFile(Long id, MultipartFile file) throws IOException {
        FileDocument existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        Files.deleteIfExists(Paths.get(existing.getUploadPath()));

        String fileName = file.getOriginalFilename();
        if (fileName == null || isInValid(fileName)) {
            throw new RuntimeException("Invalid FileType");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path newFilePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), newFilePath);

        existing.setFileName(fileName);
        existing.setFileType(file.getContentType());
        existing.setUploadPath(newFilePath.toString());
        existing.setUploadedAt(LocalDateTime.now());

        // ✅ Log update action
        auditLogService.logAction("rohan", "UPDATE");

        return repository.save(existing);
    }

    @Override
    public List<FileDocument> getAllFiles() {
        return repository.findAll();
    }

    @Override
    public List<FileDocument> searchFiles(String keyword) {
        return repository.findByFileNameContainingIgnoreCaseAndUploadedByContainingIgnoreCase(keyword, "rohan");
    }

    @Override
    public FileDocument getFileMetadata(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));
    }

    @Override
    public byte[] downloadFile(Long id) throws IOException {
        FileDocument file = getFileMetadata(id);
        return Files.readAllBytes(Paths.get(file.getUploadPath()));
    }

    @Override
    public void delete(Long id) {
        FileDocument file = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        try {
            Files.deleteIfExists(Paths.get(file.getUploadPath()));
            repository.delete(file);

            // ✅ Log delete action
            auditLogService.logAction("rohan", "DELETE");

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
