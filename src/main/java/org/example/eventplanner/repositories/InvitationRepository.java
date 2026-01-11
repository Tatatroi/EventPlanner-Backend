package org.example.eventplanner.repositories;

import org.example.eventplanner.models.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEvent_IdEventAndStatus(Long eventId, String accepted);
    Invitation findByEvent_IdEventAndEmail(Long eventId, String email);
}
