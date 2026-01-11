package org.example.eventplanner.services;

import lombok.Getter;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Getter
    private final EventRepository eventRepository;

    private final EventUserRepository eventUserRepository;

    private final InvitationRepository invitationRepository;

    private final EmailService emailService;

    public EventService(EventRepository eventRepository, EventUserRepository eventUserRepository, InvitationRepository invitationRepository, EmailService emailService) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
        this.invitationRepository = invitationRepository;
        this.emailService = emailService;
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event createEvent(Event event, Long userId) {

        Event saved = eventRepository.save(event);

        EventUser eventUser = new EventUser();
        eventUser.setIdUser(userId);
        eventUser.setIdEvent(event.getIdEvent());
        eventUser.setRole("Organizer");
        eventUser.setInvitation_status("Accepted");
        eventUser.setConfirmed(true);
        eventUserRepository.save(eventUser);

        return saved;
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
}
