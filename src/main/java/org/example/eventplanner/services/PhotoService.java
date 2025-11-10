package org.example.eventplanner.services;

import org.example.eventplanner.models.Photo;
import org.example.eventplanner.repositories.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {
    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    public Photo findById(Long id) {
        return photoRepository.findById(id).orElse(null);
    }

    public Photo save(Photo photo) {
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

    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }
}
