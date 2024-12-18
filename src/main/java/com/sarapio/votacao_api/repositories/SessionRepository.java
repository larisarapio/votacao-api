package com.sarapio.votacao_api.repositories;

import com.sarapio.votacao_api.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findById(Long id);
    boolean existsByTopicId(Long topicId);
}