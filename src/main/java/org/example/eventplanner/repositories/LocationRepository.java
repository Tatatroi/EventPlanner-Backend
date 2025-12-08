package org.example.eventplanner.repositories;

import org.example.eventplanner.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
