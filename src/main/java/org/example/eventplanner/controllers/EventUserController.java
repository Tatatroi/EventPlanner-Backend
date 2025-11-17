package org.example.eventplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.services.EventUserService;
import org.springframework.http.ResponseEntity;
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
    public EventUser enrollUserToEvent(@RequestParam Long eventId, @RequestParam Long userId) {
        return eventUserService.addEventUser(eventId, userId);
    }

    @PutMapping("/{eventId}/{userId}")
    public EventUser updateEventUser(@PathVariable Long eventId, @PathVariable Long userId, @RequestBody EventUser updated) {
        return eventUserService.updateEventUser(userId, eventId, updated);
    }

    @DeleteMapping("/{eventId}/{userId}")
    public void deleteUserFromEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserService.deleteEventUser(userId, eventId);
    }

    @GetMapping("/event/{eventId}")
    public List<EventUser> getUsersByEventId(@PathVariable Long eventId) {
        return eventUserService.getAllUsersByEventId(eventId);
    }

    @GetMapping("/user/{userId}")
    public List<EventUser> getEventsByUserId(@PathVariable Long userId) {
        return eventUserService.getAllEventsByUserId(userId);
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteUsers(@RequestParam Long idEvent, @RequestBody List<String> emails){
        eventUserService.inviteUsersToEvent(idEvent, emails);
        return ResponseEntity.ok("Invitations sent successfully.");
    }

}
