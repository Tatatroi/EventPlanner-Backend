package org.example.eventplanner.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.models.User;
import org.example.eventplanner.services.InvitationService;
import org.example.eventplanner.services.LocationService;
import org.example.eventplanner.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private static final String FRONTEND_URL = "http://localhost:3000";
    private final InvitationService invitationService;

    private final UserService userService;

    @GetMapping
    public List<Invitation> getAllInvitations() {
        return invitationService.getAllInvitations();
    }

    @GetMapping("/{id}")
    public Invitation getInvitationById(@PathVariable Long id) {
        return invitationService.getInvitationById(id);
    }

    @PostMapping
    public Invitation createInvitation(@RequestBody Invitation invitation) {
        return invitationService.createInvitation(invitation);
    }

    @GetMapping("/accept")
    public void acceptInvitation(@RequestParam Long eventId,
                                 @RequestParam String email,
                                 HttpServletResponse response) throws IOException {
        try {
            invitationService.respondToInvitation(eventId, email, true);

            User existingUser = userService.getUserRepository().findByEmail(email);

            if (existingUser != null) {
                if ("Guest".equalsIgnoreCase(existingUser.getRole())) {
                    response.sendRedirect(FRONTEND_URL + "/register?email=" + email);
                } else {
                    response.sendRedirect(FRONTEND_URL + "/login");
                }
            } else {
                response.sendRedirect(FRONTEND_URL + "/register?email=" + email);
            }

        } catch (Exception e) {
            response.sendRedirect(FRONTEND_URL + "/home?error=" + e.getMessage());
        }
    }

    @GetMapping("/decline")
    public void declineInvitation(@RequestParam Long eventId,
                                  @RequestParam String email,
                                  HttpServletResponse response) throws IOException {
        try {
            invitationService.respondToInvitation(eventId, email, false);
            response.sendRedirect(FRONTEND_URL + "/home");
        } catch (Exception e) {
            response.sendRedirect(FRONTEND_URL + "/home?error=" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Invitation updateInvitation(@PathVariable Long id, @RequestBody Invitation updatedInvitation) {
        return invitationService.updateInvitation(id, updatedInvitation);
    }

    @DeleteMapping("/{id}")
    public void deleteInvitation(@PathVariable Long id) {
            invitationService.deleteInvitation(id);
    }
}
