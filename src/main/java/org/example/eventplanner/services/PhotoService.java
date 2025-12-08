package org.example.eventplanner.services;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.Photo;
import org.example.eventplanner.services.FileStorageService;
import org.example.eventplanner.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final FileStorageService fileStorageService;
    private final PhotoRepository photoRepository;


    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public Photo getPhotoById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    public Photo createPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Photo updatePhoto(Long id, Photo updatedPhoto) {
        return photoRepository.findById(id)
                .map(photo -> {
                    photo.setFile_path(updatedPhoto.getFile_path());
                    photo.setUpload_time(updatedPhoto.getUpload_time());
                    photo.setEvent(updatedPhoto.getEvent());
                    photo.setUser(updatedPhoto.getUser());
                    return photoRepository.save(photo);
                })
                .orElseThrow(() -> new RuntimeException("Photo not found with id " + id));
    }

    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found with id: " + id));

        try {
            fileStorageService.deleteFile(photo.getFile_path());
        } catch (Exception e){
            System.out.println("Failed to delete file: " + photo.getFile_path());
        }

        photoRepository.delete(photo);

    }

    public List<Photo> getPhotosByEventId(Long eventId) {
        return photoRepository.findByEvent_IdEvent(eventId);
    }
}