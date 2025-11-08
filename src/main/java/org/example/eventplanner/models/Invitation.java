package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "invitation")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_invitation;

    @ManyToOne
    @JoinColumn(name = "id_event", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String email;

    private LocalDateTime sent_time;

    @Column(nullable = false)
    private String status; // sent / accepted / declined

    private String confirmation_code;
}
