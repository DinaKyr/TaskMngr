package com.example.TaskMngr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TaskMngr.models.Invite;

public interface RepositoryInvite extends JpaRepository<Invite, Long> {

    boolean existsByEmailAndUsedFalse(String email);

    Optional<Invite> findByToken(String token);

}
