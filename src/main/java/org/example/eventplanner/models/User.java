package org.example.eventplanner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.example.eventplanner.dto.LogInRequest;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false, unique = true)
    private String email;

    @Getter
    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private String role;

    // Relație cu EventUser (N-N)
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<EventUser> eventUsers;

    // Relație cu Photo
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Photo> photos;
}