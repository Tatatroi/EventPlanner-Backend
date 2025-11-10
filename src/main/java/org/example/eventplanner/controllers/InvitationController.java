package org.example.eventplanner.controllers;

import lombok.RequiredArgsConstructor;
import org.example.eventplanner.models.Invitation;
import org.example.eventplanner.services.InvitationService;
import org.example.eventplanner.services.LocationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;

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

    @PutMapping("/{id}")
    public Invitation updateInvitation(@PathVariable Long id, @RequestBody Invitation updatedInvitation) {
        return invitationService.updateInvitation(id, updatedInvitation);
    }

    @DeleteMapping("/{id}")
    public void deleteInvitation(@PathVariable Long id) {
        invitationService.deleteInvitation(id);
    }
}
