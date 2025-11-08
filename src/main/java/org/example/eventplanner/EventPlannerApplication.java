package org.example.eventplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.example.eventplanner.models"})
public class EventPlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventPlannerApplication.class, args);
    }

}
