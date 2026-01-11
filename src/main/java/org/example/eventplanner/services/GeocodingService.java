package org.example.eventplanner.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class GeocodingService {

    public double[] getCoordinates(String address) {
        try {
            // Curățăm adresa și o pregătim pentru URL
            String query = address.replace(" ", "+");
            // Adăugăm Cluj-Napoca pentru precizie dacă adresa e scurtă
            if (!query.toLowerCase().contains("cluj")) {
                query += "+Cluj-Napoca";
            }

            String url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&q=" + query;

            RestTemplate restTemplate = new RestTemplate();

            // OpenStreetMap cere un User-Agent obligatoriu
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "EventPlannerStudentProject/1.0");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (root.isArray() && root.size() > 0) {
                JsonNode firstResult = root.get(0);
                double lat = firstResult.get("lat").asDouble();
                double lon = firstResult.get("lon").asDouble();
                return new double[]{lat, lon};
            }
        } catch (Exception e) {
            System.err.println("Geocoding error: " + e.getMessage());
        }
        // Returnăm null sau 0 dacă eșuează
        return new double[]{0.0, 0.0};
    }
}