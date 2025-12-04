package org.example.eventplanner.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhotoDto {
    private Long idPhoto;
    private Long idEvent;
    private Long idUser;
    private String userName;
    private String filePath;
    private LocalDateTime uploadTime;
    private String fileUrl;
}