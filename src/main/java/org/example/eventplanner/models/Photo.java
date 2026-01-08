package org.example.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString(exclude = {"event", "user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "photo")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long idPhoto;

    // Removed cascade = CascadeType.ALL to prevent deleting the Event when a Photo is deleted
    @ManyToOne
    @JoinColumn(name = "id_event")
    @JsonIgnore
    private Event event;

    // Removed cascade = CascadeType.ALL to prevent deleting the User when a Photo is deleted
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(nullable = false)
    private String file_path;

    private LocalDateTime upload_time;
}