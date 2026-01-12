package org.example.eventplanner.services;

import org.example.eventplanner.dto.EventUserDto;
import org.example.eventplanner.mappers.EventUserMapper;
import org.example.eventplanner.models.*;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.InvitationRepository;
import org.example.eventplanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventUserService {

    private final EventUserRepository eventUserRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final InvitationRepository invitationRepository;
    private final EmailService emailService;

    public EventUserService(
            EventUserRepository eventUserRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            EmailService emailService,
            InvitationRepository invitationRepository
    ) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.invitationRepository = invitationRepository;
    }

    public List<EventUserDto> getAllEventUsers() {
        return eventUserRepository.findAll()
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    public Optional<EventUserDto> getEventUserByIds(Long eventId, Long userId) {
        EventUserId id = new EventUserId(userId, eventId);
        return eventUserRepository.findById(id).map(EventUserMapper::toDto);
    }

    public EventUserDto addEventUser(Long userId, Long eventId) {
        EventUser eu = new EventUser();
        eu.setIdUser(userId);
        eu.setIdEvent(eventId);
        eu.setRole("attendee");
        eu.setInvitation_status("pending");
        eu.setConfirmed(false);

        return EventUserMapper.toDto(eventUserRepository.save(eu));
    }

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

    public void deleteEventUser(Long userId, Long eventId) {
        EventUserId id = new EventUserId(userId, eventId);
        eventUserRepository.deleteById(id);
    }

    public List<EventUserDto> getAllUsersByEventId(Long eventId) {
        return eventUserRepository.findByIdEvent(eventId)
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    public List<EventUserDto> getAllEventsByUserId(Long userId) {
        return eventUserRepository.findByIdUser(userId)
                .stream()
                .map(EventUserMapper::toDto)
                .toList();
    }

    public void inviteUsersToEvent(Long idEvent, List<String> emails) {

        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Event not found for ID " + idEvent));

        for (String email : emails) {

            User user = userRepository.findByEmail(email);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setName("Guest");
                user.setLast_name("User");
                user.setPassword(UUID.randomUUID().toString());
                user.setRole("Guest");
                user = userRepository.save(user);
            }

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

            Invitation existingInv = invitationRepository.findByEvent_IdEventAndEmail(idEvent, email);

            if (existingInv == null) {
                Invitation invitation = new Invitation();
                invitation.setEvent(event);
                invitation.setEmail(email);
                invitation.setStatus("Sent");
                invitation.setSent_time(LocalDateTime.now());

                invitationRepository.save(invitation);
            }

            emailService.sendEventInvitation(email, event.getName(), idEvent);
        }
    }

    public void confirmParticipation(Long eventId, Long userId) {
        EventUserId id = new EventUserId(userId, eventId);

        EventUser eventUser = eventUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User is not invited to this event"));

        eventUser.setConfirmed(true);
        eventUser.setInvitation_status("accepted");

        eventUserRepository.save(eventUser);
    }
}