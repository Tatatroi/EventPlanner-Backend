package org.example.eventplanner.dto;

import lombok.Data;

@Data
public class EventUserResponseDto {
    private Long idUser;
    private Long idEvent;
    private String role;
    private Boolean confirmed;
    private String invitationStatus;
    private String email;
    private String name;
}
