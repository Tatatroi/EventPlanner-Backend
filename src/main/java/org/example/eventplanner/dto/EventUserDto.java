package org.example.eventplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUserDto {
    private Long userId;
    private Long eventId;
    private String email;
    private String role;
    private Boolean confirmed;
    private String invitationStatus;
}