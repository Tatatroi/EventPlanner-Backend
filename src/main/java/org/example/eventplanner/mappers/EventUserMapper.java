package org.example.eventplanner.mappers;

import org.example.eventplanner.dto.EventUserDto;
import org.example.eventplanner.models.EventUser;

public class EventUserMapper {

    public static EventUserDto toDto(EventUser eventUser) {
        if (eventUser == null) {
            return null;
        }

        EventUserDto dto = new EventUserDto();

        // 2. Setăm câmpurile unul câte unul (mult mai sigur așa)
        dto.setUserId(eventUser.getIdUser());
        dto.setEventId(eventUser.getIdEvent());
        dto.setRole(eventUser.getRole());
        dto.setConfirmed(eventUser.getConfirmed());

        dto.setInvitationStatus(eventUser.getInvitation_status());

        if (eventUser.getUser() != null) {
            dto.setEmail(eventUser.getUser().getEmail());
            // Dacă ai și nume în DTO, poți adăuga:
            // dto.setName(eventUser.getUser().getName());
        }

        return dto;
    }
}