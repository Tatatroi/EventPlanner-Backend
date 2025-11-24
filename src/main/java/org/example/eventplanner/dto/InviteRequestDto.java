package org.example.eventplanner.dto;

import lombok.Data;
import java.util.List;

@Data
public class InviteRequestDto {
    private Long idEvent;
    private List<String> emails;
}
