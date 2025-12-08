package org.example.eventplanner.dto;

public record EventUserDto(
        Long userId,
        Long eventId,
        String email,
        String role,
        Boolean confirmed,
        String invitationStatus
) {}
