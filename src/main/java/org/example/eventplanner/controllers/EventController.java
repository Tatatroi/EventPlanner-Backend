package org.example.eventplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.dto.EventDto;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public Event createEvent(@RequestBody EventDto event, @RequestParam Long userId) {
        return eventService.createEvent(event, userId );
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return eventService.updateEvent(id, updatedEvent);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }

    // În EventController.java

    @PostMapping("/{id}/notify-guests")
    public ResponseEntity<String> sendEventUpdate(@PathVariable Long id) {
        eventService.sendEventUpdateToGuests(id);
        return ResponseEntity.ok("Emails sent successfully to confirmed guests.");
    }

    @GetMapping("/user/{userId}")
    public List<EventDto> getEventsForDashboard(@PathVariable Long userId) {
        return eventService.getEventsForUserWithRole(userId);
    }

}
