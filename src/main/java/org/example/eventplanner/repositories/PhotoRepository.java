package org.example.eventplanner.repositories;

import org.example.eventplanner.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByEvent_IdEvent(Long eventId);
    List<Photo> findByUser_IdUser(Long userId);
}