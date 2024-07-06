package dev.coder.booker.service;


import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, Long id);
}
