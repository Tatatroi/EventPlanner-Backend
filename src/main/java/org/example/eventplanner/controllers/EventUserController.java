package org.example.eventplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.dto.EventUserDto;
import org.example.eventplanner.dto.EventUserResponseDto;
import org.example.eventplanner.dto.InviteRequestDto;
import org.example.eventplanner.dto.UpdateEventUserDto;
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

    // GET ALL
    @GetMapping
    public List<EventUserDto> getAllEventUsers() {
        return eventUserService.getAllEventUsers();
    }

    // GET specific
    @GetMapping("/{eventId}/{userId}")
    public Optional<EventUserDto> getEventUser(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventUserService.getEventUserByIds(eventId, userId);
    }

    // ENROLL
    @PostMapping
    public EventUserDto enrollUserToEvent(@RequestParam Long eventId, @RequestParam Long userId) {
        return eventUserService.addEventUser(eventId, userId);
    }

    // UPDATE
    @PutMapping("/{eventId}/{userId}")
    public EventUserDto updateEventUser(
            @PathVariable Long eventId,
            @PathVariable Long userId,
            @RequestBody EventUser updated
    ) {
        return eventUserService.updateEventUser(userId, eventId, updated);
    }

    // DELETE
    @DeleteMapping("/{eventId}/{userId}")
    public ResponseEntity<String> deleteUserFromEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        eventUserService.deleteEventUser(userId, eventId);
        return ResponseEntity.ok("Deleted");
    }

    // GET participants by event
    @GetMapping("/event/participants/{eventId}")
    public List<EventUserDto> getUsersByEventId(@PathVariable Long eventId) {
        return eventUserService.getAllUsersByEventId(eventId);
    }

    // GET events by user
    @GetMapping("/user/{userId}")
    public List<EventUserDto> getEventsByUserId(@PathVariable Long userId) {
        return eventUserService.getAllEventsByUserId(userId);
    }

    // POST invite
    @PostMapping("/invite")
    public ResponseEntity<String> inviteUsers(@RequestBody InviteRequestDto dto) {
        eventUserService.inviteUsersToEvent(dto.getIdEvent(), dto.getEmails());
        return ResponseEntity.ok("Invitations sent succ essfully.");
    }
}
