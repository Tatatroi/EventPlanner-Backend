package org.example.eventplanner.mappers;

import org.example.eventplanner.dto.EventDto;
import org.example.eventplanner.models.Event;

public class EventMapper {

    public static EventDto toDTO(Event event) {
        return new EventDto(
                event.getIdEvent(),
                event.getName(),
                event.getStart_time(),
                event.getEnd_time(),
                event.getLocation() != null ? event.getLocation().getName() : null
        );
    }
}
