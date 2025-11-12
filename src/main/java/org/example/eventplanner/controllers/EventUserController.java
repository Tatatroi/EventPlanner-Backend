package org.example.eventplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.services.EventUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/event-users")
@RequiredArgsConstructor
public class EventUserController {

    private final EventUserService eventUserService;

    @GetMapping
    public List<EventUser> getAllEventUsers() {
        return eventUserService.getAllEventUsers();
    }


    @GetMapping("/{eventId}/{userId}")
    public Optional<EventUser> getEventUser(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventUserService.getEventUserByIds(eventId, userId);
    }

    @PostMapping
    public EventUser createEventUser(@RequestBody EventUser eventUser) {
        return eventUserService.addEventUser(eventUser);
    }

    @PutMapping("/{eventId}/{userId}")
    public EventUser updateEventUser(@PathVariable Long eventId, @PathVariable Long userId, @RequestBody EventUser updated) {
        return eventUserService.updateEventUser(userId, eventId, updated);
    }

    @DeleteMapping("/{eventId}/{userId}")
    public void deleteEventUser(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserService.deleteEventUser(userId, eventId);
    }

}
