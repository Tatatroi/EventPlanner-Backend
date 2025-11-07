package org.example.eventplanner.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String last_name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Relație cu EventUser (N-N)
    @OneToMany(mappedBy = "user")
    private Set<EventUser> eventUsers;

    // Relație cu Photo
    @OneToMany(mappedBy = "user")
    private Set<Photo> photos;
}
