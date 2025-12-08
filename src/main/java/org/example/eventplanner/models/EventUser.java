package org.example.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(EventUserId.class)
@Table(name = "event_user")
public class EventUser {

    @Id
    @Column(name = "id_user")
    private Long idUser;

    @Id
    @Column(name = "id_event")
    private Long idEvent;

    @ManyToOne
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_event", insertable = false, updatable = false)
    @JsonIgnore
    private Event event;

    @Column(nullable = false)
    private String role; // organizer / attendee

    private Boolean confirmed;

    @Column(nullable = false)
    private String invitation_status; // pending / accepted / declined

    public void setStatus(String status) {
        this.invitation_status = status;
    }
}
