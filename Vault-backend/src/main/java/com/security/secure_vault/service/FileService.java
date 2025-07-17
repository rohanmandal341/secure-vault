package com.security.secure_vault.service;
import com.security.secure_vault.model.FileDocument;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    FileDocument uploadFile(MultipartFile file) throws IOException;

    FileDocument updateFile(Long id, MultipartFile file) throws IOException;

    List<FileDocument> getAllFiles();

    List<FileDocument> searchFiles(String keyword);

    FileDocument getFileMetadata(Long id); // for viewing metadata

    byte[] downloadFile(Long id) throws IOException;

    void delete(Long id);
}

