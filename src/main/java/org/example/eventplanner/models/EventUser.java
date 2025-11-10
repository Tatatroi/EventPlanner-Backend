package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(EventUserId.class)
@Table(name = "event_user")
public class EventUser {

    @Id
    private Long id_user;

    @Id
    private Long id_event;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_event", insertable = false, updatable = false)
    private Event event;

    @Column(nullable = false)
    private String role; // organizer / attendee

    private Boolean confirmed;

    @Column(nullable = false)
    private String invitation_status; // pending / accepted / declined
}
