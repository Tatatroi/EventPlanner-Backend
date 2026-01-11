package org.example.eventplanner.mappers;

import org.example.eventplanner.dto.EventDto;
import org.example.eventplanner.models.Event;
import org.example.eventplanner.models.Location;

public class EventMapper {

    public static EventDto toDTO(Event event) {
        LocationDto locationDto = null;
        if (event.getLocation() != null) {
            Location loc = event.getLocation();
            locationDto = new LocationDto(
                    loc.getName(),
                    loc.getAddress(),
                    loc.getLatitude(),
                    loc.getLongitude()
            );
        }


        return new EventDto(
                event.getIdEvent(),
                event.getName(),
                event.getDescription(),
                event.getStart_time(),
                event.getEnd_time(),
                locationDto,
                null,
                null
        );
    }
}