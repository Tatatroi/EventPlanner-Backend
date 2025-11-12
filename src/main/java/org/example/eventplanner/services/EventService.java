package org.example.eventplanner.services;

import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.repositories.EventRepository;
import org.example.eventplanner.repositories.EventUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final EventUserRepository eventUserRepository;

    public EventService(EventRepository eventRepository, EventUserRepository eventUserRepository) {
        this.eventUserRepository = eventUserRepository;
        this.eventRepository = eventRepository;
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
        eventUser.setId_user(userId);
        eventUser.setId_event(event.getId_event());
        eventUser.setRole("Organizer");
        eventUser.setInvitation_status("Accepted");
        eventUser.setConfirmed(true);
        eventUserRepository.save(eventUser);

        return saved;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setName(updatedEvent.getName());
                    event.setStart_time(updatedEvent.getStart_time());
                    event.setEnd_time(updatedEvent.getEnd_time());
                    event.setLocation(updatedEvent.getLocation());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }


}
