package org.example.eventplanner.services;

import lombok.Getter;
import org.example.eventplanner.models.EventUser;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.EventUserRepository;
import org.example.eventplanner.repositories.InvitationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {
    @Getter
    private final InvitationRepository invitationRepository;

    private final EventUserRepository eventUserRepository;
    private final UserService userService;
    private final EmailService emailService;

    public InvitationService(InvitationRepository invitationRepository, EventUserRepository eventUserRepository, UserService userService, EmailService emailService) {
        this.eventUserRepository = eventUserRepository;
        this.invitationRepository = invitationRepository;
        this.userService = userService;
        this.emailService = emailService;
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
        // 1. Update Invitation Table
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

        // 2. Update EventUser Table (FIX-UL CRITIC)
        User user = userService.getUserRepository().findByEmail(email);
        if (user != null) {
            EventUser eventUser = eventUserRepository.findByIdUserAndIdEvent(user.getIdUser(), eventId)
                    .orElse(null);

            if (eventUser != null) {
                if (isAccepted) {
                    eventUser.setInvitation_status("accepted");
                    eventUser.setConfirmed(true);
                    try {
                        emailService.sendHtmlEmail(
                                email,
                                "Confirmation: You are going to " + invitation.getEvent().getName(),
                                "<h1>Participation Confirmed!</h1><p>See you at the event.</p>"
                        );
                    } catch (Exception e) {
                        System.err.println("Failed to send confirmation email: " + e.getMessage());
                    }

                } else {
                    eventUser.setInvitation_status("declined");
                    eventUser.setConfirmed(false);
                }
                eventUserRepository.save(eventUser);
            }
        }
    }
}
