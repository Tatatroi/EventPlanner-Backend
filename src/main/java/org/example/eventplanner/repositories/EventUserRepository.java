package org.example.eventplanner.repositories;

import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.EventUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventUserRepository extends JpaRepository<EventUser, EventUserId> {
List<EventUser> findByIdEvent(Long id_event);
List<EventUser> findByIdUser(Long id_user);
Optional<EventUser> findByIdUserAndIdEvent(Long idUser, Long idEvent);
}
