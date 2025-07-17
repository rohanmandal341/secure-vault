package com.security.secure_vault.repository;

import com.security.secure_vault.model.FileDocument;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileDocument, Long>{
    List<FileDocument> findByFileNameContainingIgnoreCaseAndUploadedByContainingIgnoreCase(String username, String fileName);
}