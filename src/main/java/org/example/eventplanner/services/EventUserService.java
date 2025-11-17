package org.example.eventplanner.services;

import jakarta.validation.constraints.Email;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.EventUserId;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventUserService {

    private final EventUserRepository eventUserRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    public EventUserService(EventUserRepository eventUserRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
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

    public void inviteUsersToEvent(Long idEvent, List<String> emails) {
        Event event = eventRepository.findById(idEvent).orElseThrow(() -> new RuntimeException("Event not found for Event ID " + idEvent));

        for (String email : emails) {
            User user = userRepository.findByEmail(email);

            if (user == null) {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName("Guest");
                newUser.setLast_name("User");
                newUser.setPassword(UUID.randomUUID().toString());
                user = userRepository.save(newUser);
            }

            EventUser existing = eventUserRepository
                    .findById_userAndId_event(user.getId_user(), event.getId_event())
                    .orElse(null);

            if (existing == null) {
                EventUser eu = new EventUser();
                eu.setUser(user);
                eu.setEvent(event);
                eu.setId_user(user.getId_user());
                eu.setId_event(event.getId_event());
                eu.setRole("attendee");
                eu.setConfirmed(false);
                eu.setInvitation_status("pending");

                eventUserRepository.save(eu);
            }

//            emailService.sendInvitation(email, event.getName(), idEvent, user.getId_user());
        }


    }
}
