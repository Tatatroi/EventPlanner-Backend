package org.example.eventplanner.services;

import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.EventUserId;
import org.example.eventplanner.repositories.EventUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventUserService {

    private final EventUserRepository eventUserRepository;

    public EventUserService(EventUserRepository eventUserRepository) {
        this.eventUserRepository = eventUserRepository;
    }

    public List<EventUser> getAllEventUsers() {
        return eventUserRepository.findAll();
    }

    public Optional<EventUser> getEventUserByIds(Long eventId, Long userId) {
        EventUserId id = new EventUserId(eventId, userId);
        return eventUserRepository.findById(id);
    }

    public EventUser addEventUser(Long userId, Long eventId) {
        EventUser savedEventUser = new EventUser();
        savedEventUser.setId_user(userId);
        savedEventUser.setId_event(eventId);
        savedEventUser.setRole("attendee");
        savedEventUser.setInvitation_status("pending");
        savedEventUser.setConfirmed(false);

        return eventUserRepository.save(savedEventUser);
    }

    public EventUser updateEventUser(Long userId, Long eventId, EventUser updatedEventUser) {
        EventUserId id = new EventUserId(userId, eventId);
        return eventUserRepository.findById(id)
                .map(existing -> {
                    existing.setRole(updatedEventUser.getRole());
                    existing.setInvitation_status(updatedEventUser.getInvitation_status());
                    existing.setConfirmed(updatedEventUser.getConfirmed());
                    return eventUserRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("EventUser not found for Event ID " + eventId + " and User ID " + userId));
    }

    public void deleteEventUser(Long userId, Long eventId) {
        EventUserId id = new EventUserId(userId, eventId);
        eventUserRepository.deleteById(id);
    }

    public List<EventUser> getAllUsersByEventId(Long eventId) {
        return eventUserRepository.findByid_event(eventId);
    }

    public List<EventUser> getAllEventsByUserId(Long userId) {
        return eventUserRepository.findByid_user(userId);
    }
}
