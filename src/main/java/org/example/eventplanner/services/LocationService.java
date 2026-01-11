//package org.example.eventplanner.services;
//
//
//import org.example.eventplanner.models.Location;
//import org.example.eventplanner.repositories.LocationRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class LocationService {
//    private final LocationRepository locationRepository;
//
//    public LocationService(LocationRepository locationRepository) {
//        this.locationRepository = locationRepository;
//    }
//
//    public List<Location> getAllLocations() {
//        return locationRepository.findAll();
//    }
//
//    public Location getLocationById(Long id) {
//        return locationRepository.findById(id).orElse(null);
//    }
//
//    public Location createLocation(Location location) {
//        return locationRepository.save(location);
//    }
//
//    public Location updateLocation(Long id, Location updatedLocation) {
//        return locationRepository.findById(id)
//                .map(location -> {
//                    location.setAddress(updatedLocation.getAddress());
//                    location.setName(updatedLocation.getName());
//                    location.setLatitude(updatedLocation.getLatitude());
//                    location.setLongitude(updatedLocation.getLongitude());
//                    return locationRepository.save(location);
//                })
//                .orElseThrow(() -> new RuntimeException("Location not found with id " + id));
//    }
//
//    public void deleteLocation(Long id) {
//        locationRepository.deleteById(id);
//    }
//
//}
package org.example.eventplanner.services;

import org.example.eventplanner.models.Location;
import org.example.eventplanner.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final GeocodingService geocodingService; // 1. Injectăm serviciul nou

    // 2. Îl adăugăm în constructor
    public LocationService(LocationRepository locationRepository, GeocodingService geocodingService) {
        this.locationRepository = locationRepository;
        this.geocodingService = geocodingService;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public Location createLocation(Location location) {
        // 3. Calculăm coordonatele înainte de salvare
        enrichLocationWithCoordinates(location);
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(location -> {
                    location.setAddress(updatedLocation.getAddress());
                    location.setName(updatedLocation.getName());

                    // Actualizăm și coordonatele manuale dacă sunt oferite
                    location.setLatitude(updatedLocation.getLatitude());
                    location.setLongitude(updatedLocation.getLongitude());

                    // 4. Dacă s-a schimbat adresa și nu avem coordonate valide, le recalculăm
                    enrichLocationWithCoordinates(location);

                    return locationRepository.save(location);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id " + id));
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    // --- Metodă ajutătoare pentru a ține codul curat ---
    private void enrichLocationWithCoordinates(Location location) {
        // Dacă avem o adresă, dar NU avem coordonate (sau sunt 0), le cerem de la OpenStreetMap
        if (location.getAddress() != null && !location.getAddress().isEmpty()) {
            if (location.getLatitude() == null || location.getLatitude() == 0.0 ||
                    location.getLongitude() == null || location.getLongitude() == 0.0) {

                double[] coords = geocodingService.getCoordinates(location.getAddress());

                // Setăm noile valori
                if (coords[0] != 0.0 && coords[1] != 0.0) {
                    location.setLatitude(coords[0]);
                    location.setLongitude(coords[1]);
                }
            }
        }
    }
}