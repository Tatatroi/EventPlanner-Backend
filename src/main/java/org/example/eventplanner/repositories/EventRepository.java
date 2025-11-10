package org.example.eventplanner.repositories;


import org.example.eventplanner.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
