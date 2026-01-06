package com.example.TaskMngr.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.TaskMngr.models.Invite;
import com.example.TaskMngr.models.Role;
import com.example.TaskMngr.repositories.RepositoryInvite;

@Service
public class InviteService {

    private final RepositoryInvite inviteRepository;

    public InviteService(RepositoryInvite inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    //new invite
    public Invite createInvite(String email, Role role) {
        if (inviteRepository.existsByEmailAndUsedFalse(email)) {
            throw new IllegalArgumentException("An active invite already exists for this email");
        }

        Invite invite = new Invite();
        invite.setEmail(email);
        invite.setRole(role);
        invite.setToken(UUID.randomUUID().toString());
        invite.setExpiresAt(LocalDateTime.now().plusDays(2)); //lasts for 2 days
        invite.setUsed(false);

        return inviteRepository.save(invite);
    }

    // Validate token
    public Invite validateInvite(String token) {
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invite token"));

        if (invite.isUsed() || invite.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invite is expired or already used");
        }

        return invite;
    }

    //Mark invite as used
    public void markUsed(Invite invite) {
        invite.setUsed(true);
        inviteRepository.save(invite);
    }
}
