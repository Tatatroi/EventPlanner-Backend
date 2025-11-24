package org.example.eventplanner.services;

import org.example.eventplanner.dto.EventUserDto;
import org.example.eventplanner.mappers.EventUserMapper;
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
    private final EmailService emailService;

    public EventUserService(
            EventUserRepository eventUserRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // --------------------------------------------------------------------
    // GET ALL
    // --------------------------------------------------------------------
    public List<EventUserDto> getAllEventUsers() {
        return eventUserRepository.findAll()
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    // --------------------------------------------------------------------
    // GET BY IDs
    // --------------------------------------------------------------------
    public Optional<EventUserDto> getEventUserByIds(Long eventId, Long userId) {
        EventUserId id = new EventUserId(userId, eventId);
        return eventUserRepository.findById(id).map(EventUserMapper::toDto);
    }

    // --------------------------------------------------------------------
    // ADD USER TO EVENT
    // --------------------------------------------------------------------
    public EventUserDto addEventUser(Long userId, Long eventId) {
        EventUser eu = new EventUser();
        eu.setIdUser(userId);
        eu.setIdEvent(eventId);
        eu.setRole("attendee");
        eu.setInvitation_status("pending");
        eu.setConfirmed(false);

        return EventUserMapper.toDto(eventUserRepository.save(eu));
    }

    // --------------------------------------------------------------------
    // UPDATE EventUser
    // --------------------------------------------------------------------
    public EventUserDto updateEventUser(Long userId, Long eventId, EventUser updatedEventUser) {
        EventUserId id = new EventUserId(userId, eventId);

        EventUser updated = eventUserRepository.findById(id)
                .map(existing -> {
                    existing.setRole(updatedEventUser.getRole());
                    existing.setInvitation_status(updatedEventUser.getInvitation_status());
                    existing.setConfirmed(updatedEventUser.getConfirmed());
                    return eventUserRepository.save(existing);
                })
                .orElseThrow(() ->
                        new RuntimeException("EventUser not found for Event ID " + eventId + " and User ID " + userId)
                );

        return EventUserMapper.toDto(updated);
    }

    // --------------------------------------------------------------------
    // DELETE FROM EVENT
    // --------------------------------------------------------------------
    public void deleteEventUser(Long userId, Long eventId) {
        EventUserId id = new EventUserId(userId, eventId);
        eventUserRepository.deleteById(id);
    }

    // --------------------------------------------------------------------
    // GET ALL USERS OF AN EVENT
    // --------------------------------------------------------------------
    public List<EventUserDto> getAllUsersByEventId(Long eventId) {
        return eventUserRepository.findByIdEvent(eventId)
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    // --------------------------------------------------------------------
    // GET ALL EVENTS OF A USER
    // --------------------------------------------------------------------
    public List<EventUserDto> getAllEventsByUserId(Long userId) {
        return eventUserRepository.findByIdUser(userId)
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    // --------------------------------------------------------------------
    // SEND INVITATIONS
    // --------------------------------------------------------------------
    public void inviteUsersToEvent(Long idEvent, List<String> emails) {

        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Event not found for ID " + idEvent));

        for (String email : emails) {

            // 1. găsim user sau creăm unul nou
            User user = userRepository.findByEmail(email);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName("Guest");
                user.setLast_name("User");
                user.setPassword(UUID.randomUUID().toString());
                user = userRepository.save(user);
            }

            // 2. verificăm dacă participantul există deja
            EventUser existing = eventUserRepository
                    .findByIdUserAndIdEvent(user.getIdUser(), idEvent)
                    .orElse(null);

            if (existing == null) {
                EventUser eu = new EventUser();
                eu.setUser(user);
                eu.setEvent(event);
                eu.setIdUser(user.getIdUser());
                eu.setIdEvent(event.getIdEvent());
                eu.setRole("attendee");
                eu.setConfirmed(false);
                eu.setInvitation_status("pending");

                eventUserRepository.save(eu);
            }

            // 3. trimitem mail
            emailService.sendEventInvitation(email, event.getName(), idEvent, user.getIdUser());
        }
    }
}
