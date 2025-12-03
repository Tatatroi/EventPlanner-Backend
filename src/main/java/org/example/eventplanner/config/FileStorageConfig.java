package org.example.eventplanner.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Configuration
public class FileStorageConfig {
    @Value("${file.upload-dir:uploads/photos}")
    private String uploadDir;

}