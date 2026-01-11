package org.example.eventplanner.services;

import lombok.Getter;
import org.example.eventplanner.dto.EventDto;
import org.example.eventplanner.mappers.EventMapper;
import org.example.eventplanner.models.*;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Getter
    private final EventRepository eventRepository;

    private final EventUserRepository eventUserRepository;

    private final InvitationRepository invitationRepository;

    private final EmailService emailService;
    private final UserService userService;

    public EventService(EventRepository eventRepository, EventUserRepository eventUserRepository, InvitationRepository invitationRepository, EmailService emailService, UserService userService) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event createEvent(EventDto eventDto, Long mainOrganizerId) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setStart_time(eventDto.getStartTime());
        event.setEnd_time(eventDto.getEndTime());

        if (eventDto.getLocation() != null) {
            Location loc = new Location();
            loc.setName(eventDto.getLocation().getName());
            loc.setAddress(eventDto.getLocation().getAddress());
            loc.setLatitude(eventDto.getLocation().getLatitude());
            loc.setLongitude(eventDto.getLocation().getLongitude());
            event.setLocation(loc);
        }

        Event savedEvent = eventRepository.save(event);

        // -------------------------------------------------------------
        // 2. SALVĂM ORGANIZATORUL PRINCIPAL
        // -------------------------------------------------------------
        User mainUser = userService.getUserRepository().findById(mainOrganizerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EventUser mainOrganizerLink = new EventUser();
        mainOrganizerLink.setUser(mainUser);
        mainOrganizerLink.setEvent(savedEvent);
        mainOrganizerLink.setIdUser(mainUser.getIdUser());
        mainOrganizerLink.setIdEvent(savedEvent.getIdEvent());

        mainOrganizerLink.setRole("organizer"); // <--- ROL PRINCIPAL
        mainOrganizerLink.setConfirmed(true);
        mainOrganizerLink.setInvitation_status("accepted");

        eventUserRepository.save(mainOrganizerLink);

        // -------------------------------------------------------------
        // 3. SALVĂM CO-ORGANIZATORUL (Dacă există email)
        // -------------------------------------------------------------
        String coEmail = eventDto.getCoOrganizerEmail();

        if (coEmail != null && !coEmail.trim().isEmpty()) {
            User coUser = userService.getUserRepository().findByEmail(coEmail);
            if (coUser == null) {
                coUser = new User();
                coUser.setEmail(coEmail);
                coUser.setName("Co-Organizer");
                coUser.setLast_name("Guest");
                coUser.setRole("Guest"); // E guest în sistem, dar Organizer la eveniment
                coUser = userService.getUserRepository().save(coUser);
            }

            // Creăm legătura în EventUser
            EventUser coOrganizerLink = new EventUser();
            coOrganizerLink.setUser(coUser);
            coOrganizerLink.setEvent(savedEvent);
            coOrganizerLink.setIdUser(coUser.getIdUser());
            coOrganizerLink.setIdEvent(savedEvent.getIdEvent());

            coOrganizerLink.setRole("organizer"); // <--- ȘI EL PRIMEȘTE ROL DE ORGANIZER

            // El trebuie să accepte invitația, deci e pending
            coOrganizerLink.setConfirmed(false);
            coOrganizerLink.setInvitation_status("pending");

            eventUserRepository.save(coOrganizerLink);

            // Opțional: Trimitem mail și lui
            emailService.sendEventInvitation(coEmail, savedEvent.getName(), savedEvent.getIdEvent());
        }

        return savedEvent;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setStart_time(updatedEvent.getStart_time());
        existingEvent.setEnd_time(updatedEvent.getEnd_time());

        if (updatedEvent.getLocation() != null) {
            if (existingEvent.getLocation() == null) {
                existingEvent.setLocation(updatedEvent.getLocation());
            } else {
                existingEvent.getLocation().setName(updatedEvent.getLocation().getName());
                existingEvent.getLocation().setAddress(updatedEvent.getLocation().getAddress());
                if (updatedEvent.getLocation().getLatitude() != null)
                    existingEvent.getLocation().setLatitude(updatedEvent.getLocation().getLatitude());
                if (updatedEvent.getLocation().getLongitude() != null)
                    existingEvent.getLocation().setLongitude(updatedEvent.getLocation().getLongitude());
            }
        }
        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }


    public void sendEventUpdateToGuests(Long eventId) {
        // 1. Găsim evenimentul
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 2. Găsim invitații confirmați
        List<Invitation> confirmedGuests = invitationRepository.findByEvent_IdEventAndStatus(eventId, "Accepted");

        if (confirmedGuests.isEmpty()) {
            throw new RuntimeException("No confirmed guests found to notify.");
        }

        String subject = "Update: " + event.getName();

        String description = (event.getDescription() != null && !event.getDescription().isEmpty())
                ? event.getDescription()
                : "No specific details provided.";

        String htmlBody = """
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2 style="color: #333;">Update for event: %s</h2>
                <p>Hello,</p>
                <p>The organizers have updated the agenda/details for this event. Please check the new info below:</p>
               \s
                <div style="background-color: #f8f9fa; padding: 15px; border-left: 5px solid #007bff; margin: 20px 0;">
                    <h3 style="margin-top: 0;">📝 Updated Description</h3>
                    <p style="white-space: pre-wrap;">%s</p>
                </div>

                <p><strong>📍 Location:</strong> %s</p>
                <p><strong>⏰ Start Time:</strong> %s</p>

                <p style="margin-top: 20px;">See you there!</p>
            </body>
            </html>
       \s""".formatted(
                event.getName(),
                description,
                event.getLocation() != null ? event.getLocation().getName() : "TBD",
                event.getStart_time()
        );

        for (Invitation invitation : confirmedGuests) {
            String guestEmail = invitation.getEmail();
            emailService.sendHtmlEmail(guestEmail, subject, htmlBody);
        }
    }

    public List<EventDto> getEventsForUserWithRole(Long userId) {
        List<EventUser> links = eventUserRepository.findByIdUser(userId);

        return links.stream()
                .map(link -> {
                    Event event = link.getEvent();

                    EventDto dto = EventMapper.toDTO(event);

                    dto.setRole(link.getRole());

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
