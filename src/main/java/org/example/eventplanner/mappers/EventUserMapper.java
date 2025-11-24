package org.example.eventplanner.mappers;

import org.example.eventplanner.dto.EventUserDto;
import org.example.eventplanner.models.EventUser;

public class EventUserMapper {

    public static EventUserDto toDto(EventUser eventUser) {
        return new EventUserDto(
                eventUser.getIdUser(),
                eventUser.getIdEvent(),
                eventUser.getUser() != null ? eventUser.getUser().getEmail() : null,
                eventUser.getRole(),
                eventUser.getConfirmed(),
                eventUser.getInvitation_status()
        );
    }

}
