package dev.coder.booker.service.implementation;

import dev.coder.booker.entity.book.Book;
import dev.coder.booker.service.FileStorageService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${application.file.upload.photos-output-dir}")
    private String fileUploadPath;
    @Override
    public String storeFile(@Nonnull MultipartFile file, @Nonnull Long userId) {
        final String fileUploadSubPath = "users"+ File.separator+ userId;
        return uploadFile(file, fileUploadSubPath);
    }

    private String uploadFile(@Nonnull MultipartFile file,@Nonnull String fileUploadSubPath) {
        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        final File uploadDir = new File(finalUploadPath);
        if (!uploadDir.exists()) {
            boolean mkdirs = uploadDir.mkdirs();
            if(!mkdirs){
                log.warn("Could not create directory {}", finalUploadPath);
                return null;
            }
        }
        final String fileName = file.getOriginalFilename();
        final String fileExtension = getFileExtension(fileName);
        final String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis() + fileExtension;
        Path path = Paths.get(targetFilePath);
        try {
            Files.write(path, file.getBytes());
            log.info("File uploaded successfully to {}", targetFilePath);
            //file.transferTo(path);
            return targetFilePath;
        } catch (Exception e) {
            log.error("Could not store file {}", targetFilePath, e);
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName != null && fileName.contains(".") && fileName.lastIndexOf(".") != -1){
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
}
