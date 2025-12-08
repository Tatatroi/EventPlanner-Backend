package org.example.eventplanner.mappers;

import org.example.eventplanner.dto.PhotoDto;
import org.example.eventplanner.models.Photo;

public class PhotoMapper {
    public static PhotoDto toDto(Photo photo){
        PhotoDto dto = new PhotoDto();
        dto.setIdPhoto(photo.getIdPhoto());
        dto.setIdEvent(photo.getEvent().getIdEvent());
        dto.setIdUser(photo.getUser().getIdUser());
        dto.setUserName(photo.getUser().getName() + " " + photo.getUser().getLast_name());
        dto.setFilePath(photo.getFile_path());
        dto.setUploadTime(photo.getUpload_time());

        return dto;
    }
}