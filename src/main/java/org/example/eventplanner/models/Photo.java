package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPhoto;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_event")
    private Event event;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(nullable = false)
    private String file_path;

    private LocalDateTime upload_time;
}
