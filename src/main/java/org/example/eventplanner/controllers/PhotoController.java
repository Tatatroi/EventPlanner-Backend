package org.example.eventplanner.controllers;


import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.Photo;
import org.example.eventplanner.services.PhotoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping
    public List<Photo> getAllPhotos() {
        return photoService.getAllPhotos();
    }

    @GetMapping("/{id}")
    public Photo getPhotoById(@PathVariable Long id) {
        return photoService.getPhotoById(id);
    }

    @PostMapping
    public Photo createPhoto(@RequestBody Photo photo) {
        return photoService.createPhoto(photo);
    }

    @PutMapping("/{id}")
    public Photo updatePhoto(@PathVariable Long id, @RequestBody Photo updatedPhoto) {
        return photoService.updatePhoto(id, updatedPhoto);
    }

    @DeleteMapping("/{id}")
    public void deletePhoto(@PathVariable Long id) {
        photoService.deletePhoto(id);
    }

}
