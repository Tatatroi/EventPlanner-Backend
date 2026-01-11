package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.Transient;

@Getter
@Setter
@ToString(exclude = {"location", "schedules", "invitations", "photos", "eventUsers"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idEvent;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime start_time;

    @Column(nullable = false)
    private LocalDateTime end_time;

    @Column(length = 1024)
    String description;

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

    @jakarta.persistence.Transient
    @JsonProperty("organizer")
    public User getOrganizer() {
        if (eventUsers == null) {
            return null;
        }
        return eventUsers.stream()
                .filter(eu -> "organizer".equalsIgnoreCase(eu.getRole()) || "Organizer".equalsIgnoreCase(eu.getRole()))
                .findFirst()
                .map(EventUser::getUser)
                .orElse(null);
    }

}