package org.example.eventplanner.dto;

import java.time.LocalDateTime;

// ce primeste backendul atunci cand creez un eveniment nou
public record CreateEventDto(
        String name,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long locationId
) {}
