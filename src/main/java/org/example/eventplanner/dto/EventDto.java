package org.example.eventplanner.dto;

import java.time.LocalDateTime;

public record EventDto(
        Long idEvent,
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String locationName
) {}
