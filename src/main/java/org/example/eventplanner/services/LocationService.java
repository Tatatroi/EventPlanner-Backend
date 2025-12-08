package org.example.eventplanner.services;


import org.example.eventplanner.models.Location;
import org.example.eventplanner.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setAddress(updatedLocation.getAddress());
                    location.setName(updatedLocation.getName());
                    location.setLatitude(updatedLocation.getLatitude());
                    location.setLongitude(updatedLocation.getLongitude());
                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id " + id));
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

}
