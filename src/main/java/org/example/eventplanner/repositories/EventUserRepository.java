package org.example.eventplanner.repositories;

import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.EventUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, EventUserId> {

}
