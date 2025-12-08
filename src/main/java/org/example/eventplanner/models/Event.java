package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvent;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private LocalDateTime end_time;

    // Relație cu Location (N:1)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_location")
    private Location location;

    // Relație cu Schedule (1:N)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<Schedule> schedules;

    // Relație cu Invitation (1:N)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<Invitation> invitations;

    // Relație cu Photo (1:N)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<Photo> photos;

    // Relație cu EventUser (N:N)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<EventUser> eventUsers;
}
