package org.example.eventplanner.services;

import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public InvitationService(InvitationRepository invitationRepository) {
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
}
