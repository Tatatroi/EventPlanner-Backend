package org.example.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.eventplanner.dto.LogInRequest;

import java.time.LocalDateTime;

@Data
@Entity
@Getter
@Setter
@Table(name = "invitation")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInvitation;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_event", nullable = false)
    @JsonIgnore
    private Event event;

    @Getter
    @Setter
    @Column(nullable = false)
    private String email;

    private LocalDateTime sent_time;

    @Column(nullable = false)
    private String status; // sent / accepted / declined

    private String confirmation_code;
}