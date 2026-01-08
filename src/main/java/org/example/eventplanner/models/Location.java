package org.example.eventplanner.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLocation;

    @Column(nullable = false)
    private String address;

    private String name;

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private Set<Event> events;
}