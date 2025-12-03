package org.example.eventplanner.controllers;


import lombok.RequiredArgsConstructor;
import org.example.eventplanner.dto.PhotoDto;
import org.example.eventplanner.mappers.PhotoMapper;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.Photo;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.UserRepository;
import org.example.eventplanner.services.FileStorageService;
import org.example.eventplanner.services.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photos")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<PhotoDto>> getAllPhotos() {
        List<PhotoDto> photos = photoService.getAllPhotos()
                .stream()
                .map(PhotoMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(photos);
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

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            Authentication authentication) {

        try {
            String filePath = fileStorageService.storeFile(file);
            String username = authentication.getName();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            Photo photo = new Photo();
            photo.setFile_path(filePath);
            photo.setEvent(event);
            photo.setUser(user);
            photo.setUpload_time(LocalDateTime.now());

            Photo savedPhoto = photoService.createPhoto(photo);
            PhotoDto photoDto = PhotoMapper.toDto(savedPhoto);

            return ResponseEntity.status(HttpStatus.CREATED).body(photoDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}