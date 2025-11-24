package org.example.eventplanner.dto;

public record UserDto(
        Long idUser,
        String name,
        String lastName,
        String email
) {}
