package org.example.eventplanner.controllers;


import lombok.RequiredArgsConstructor;
import org.example.eventplanner.dto.PhotoDto;
import org.example.eventplanner.mappers.PhotoMapper;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.Photo;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.UserRepository;
import org.example.eventplanner.services.FileStorageService;
import org.example.eventplanner.services.PhotoService;
import org.example.eventplanner.services.UserService;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final UserService userService;

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
    public ResponseEntity<?> deletePhoto(@PathVariable Long id, @RequestParam Long userId) {
        System.out.println("DELETE photoId = " + id + ", userId = " + userId);

        try {
            System.out.println(id);
            Photo photo = photoService.getPhotoById(id);
            if (photo == null) {
                System.out.println("IS NULL");
                return ResponseEntity.notFound().build();
            }

            // Get data BEFORE any modifications
            Long uploaderId = photo.getUser().getIdUser();
            Event event = photo.getEvent();

            boolean isUploader = uploaderId.equals(userId);
            boolean isOrganizer = event.getEventUsers().stream()
                    .anyMatch(eventUser ->
                            eventUser.getUser().getIdUser().equals(userId) &&
                                    "Organizer".equalsIgnoreCase(eventUser.getRole())
                    );

            if (!isUploader && !isOrganizer) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to delete this photo");
            }

            // Store file path before deletion
            String filePath = photo.getFile_path();

            // Delete from database first
            try {
                System.out.println("ID ID ID ID ID " + id);
                photoService.deletePhoto(id);
            } catch (Exception e){
                return ResponseEntity.badRequest().body("Error comes from service");
            }

            // Then delete physical file
            try {
                fileStorageService.deleteFile(filePath);
            } catch (Exception fileEx) {
                System.err.println("Warning: Could not delete file: " + fileEx.getMessage());
            }

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            return ResponseEntity.badRequest()
                    .body("Error deleting photo: " + errorMessage);
        }
    }


    @GetMapping("/files/{photoId}")
    public ResponseEntity<Resource> getPhotoFile(@PathVariable Long photoId) {
        try {
            Photo photo = photoService.getPhotoById(photoId);
            if (photo == null) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = fileStorageService.loadFileAsResource(photo.getFile_path());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("eventId") Long eventId,
            @RequestParam Long userId) {

        try {
            String filePath = fileStorageService.storeFile(file);

            User user = userRepository.findById(userId).orElseThrow( () -> new RuntimeException("User not found"));
            if (user == null){
                throw new RuntimeException("User not found");
            }
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            Photo photo = new Photo();
            photo.setFile_path(filePath);
            photo.setEvent(event);
            photo.setUser(user);
            photo.setUpload_time(LocalDateTime.now());

            Photo savedPhoto = photoService.createPhoto(photo);
            PhotoDto photoDto = PhotoMapper.toDto(savedPhoto);
            photoDto.setFileUrl("/api/photos/files/" + savedPhoto.getIdPhoto());

            return ResponseEntity.status(HttpStatus.CREATED).body(photoDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<PhotoDto>> getPhotosByEvent(@PathVariable Long eventId) {
        try {
            List<PhotoDto> photos = photoService.getPhotosByEventId(eventId)
                    .stream()
                    .map(PhotoMapper::toDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}