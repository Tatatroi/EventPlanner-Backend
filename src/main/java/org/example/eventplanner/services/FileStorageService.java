package org.example.eventplanner.services;

import org.apache.commons.io.FilenameUtils;
import org.example.eventplanner.config.FileStorageConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    @Value("${file.max-size:5242880}")
    private long maxFileSize;

    @Value("{file.allowed-extensions:jpg,jpeg,png}")
    private String[] allowedExtensions;

    public FileStorageService(FileStorageConfig config) throws IOException {
        this.fileStorageLocation = Paths.get(config.getUploadDir()).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String newFilename = UUID.randomUUID().toString() + "." + extension;

        Path targetLocation = this.fileStorageLocation.resolve(newFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        return newFilename;
    }

    private void validateFile(MultipartFile file){
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size");
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        if (!Arrays.asList(allowedExtensions).contains(extension)){
            throw new IllegalArgumentException("File type not allowed. Allowed types: " + String.join(",", allowedExtensions));
        }
    }

    public void deleteFile(String filename) throws IOException {
        Path filePath = this.fileStorageLocation.resolve(filename).normalize();
        Files.deleteIfExists(filePath);
    }

    public Resource loadFileAsResource(String filePath) {

        try {
            Path file = this.fileStorageLocation.resolve(filePath).normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found " + filePath, e);
        }
    }
}