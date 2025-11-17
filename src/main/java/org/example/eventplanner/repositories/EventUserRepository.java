package org.example.eventplanner.repositories;

import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.EventUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, EventUserId> {
List<EventUser> findByid_event(Long id_event);
List<EventUser> findByid_user(Long id_user);
Optional<EventUser> findById_userAndId_event(Long idUser, Long idEvent);
}
