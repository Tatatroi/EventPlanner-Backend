package org.example.eventplanner.services;

import lombok.Getter;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    @Getter
    private final InvitationRepository invitationRepository;

    private final EventUserRepository eventUserRepository;

    public InvitationService(InvitationRepository invitationRepository, EventUserRepository eventUserRepository) {
        this.eventUserRepository = eventUserRepository;
        this.invitationRepository = invitationRepository;
    }

    public List<Invitation> getAllInvitations() {
        return invitationRepository.findAll();
    }

    public Invitation getInvitationById(Long id) {
        return invitationRepository.findById(id).orElse(null);
    }

    public Invitation createInvitation(Invitation invitation) {
        return invitationRepository.save(invitation);
    }

    public Invitation updateInvitation(Long id, Invitation updatedInvitation) {
        return invitationRepository.findById(id)
                .map(invitation -> {
                    invitation.setEmail(updatedInvitation.getEmail());
                    invitation.setStatus(updatedInvitation.getStatus());
                    invitation.setConfirmation_code(updatedInvitation.getConfirmation_code());
                    invitation.setSent_time(updatedInvitation.getSent_time());
                    return invitationRepository.save(invitation);
                })
                .orElseThrow(() -> new RuntimeException("Invitation not found with id " + id));
    }

    public void deleteInvitation(Long id) {
        invitationRepository.deleteById(id);
    }

    public void acceptInvitation(Long eventId, Long userId) {
        EventUser eu = eventUserRepository.findByIdUserAndIdEvent(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        eu.setStatus("accepted");
        eventUserRepository.save(eu);
    }

    public void declineInvitation(Long eventId, Long userId) {
        EventUser eu = eventUserRepository.findByIdUserAndIdEvent(userId, eventId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
        eu.setStatus("declined");
        eventUserRepository.save(eu);
    }

    public void respondToInvitation(Long eventId, String email, boolean isAccepted) {
        Invitation invitation = invitationRepository.findByEvent_IdEventAndEmail(eventId, email);
        if (invitation == null) {
            throw new RuntimeException("Invitation not found for email: " + email);
        }
        if (isAccepted) {
            invitation.setStatus("Accepted");
        } else {
            invitation.setStatus("Declined");
        }
        invitationRepository.save(invitation);
    }
}
