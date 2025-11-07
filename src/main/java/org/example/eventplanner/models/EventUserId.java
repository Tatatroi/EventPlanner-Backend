package org.example.eventplanner.models;

import lombok.Data;
import java.io.Serializable;

@Data
public class EventUserId implements Serializable {
    private Long id_user;
    private Long id_event;
}
