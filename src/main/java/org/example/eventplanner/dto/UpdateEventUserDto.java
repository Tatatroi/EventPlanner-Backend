package org.example.eventplanner.dto;

import lombok.Data;

@Data
public class UpdateEventUserDto {
    private String role;
    private Boolean confirmed;
    private String invitationStatus;
}
