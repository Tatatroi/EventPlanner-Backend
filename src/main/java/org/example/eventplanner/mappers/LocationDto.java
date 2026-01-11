package org.example.eventplanner.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDto {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}