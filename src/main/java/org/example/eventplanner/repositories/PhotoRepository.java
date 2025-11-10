package org.example.eventplanner.repositories;

import org.example.eventplanner.models.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}
