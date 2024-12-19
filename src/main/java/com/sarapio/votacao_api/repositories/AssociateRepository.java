package com.sarapio.votacao_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarapio.votacao_api.domain.Associate;

import java.util.Optional;
import java.util.UUID;

public interface AssociateRepository extends JpaRepository<Associate, UUID> {
    Optional<Associate> findByCpf(String cpf);
}