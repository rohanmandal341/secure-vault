package com.security.secure_vault.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
@Builder
@Entity
public class FileDocument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fileName;
  private String fileType;
  private String uploadPath;
  private String uploadedBy;

  private LocalDateTime uploadedAt;

}
