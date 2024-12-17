package com.sarapio.votacao_api.repositories;

import com.sarapio.votacao_api.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    boolean existsByTitle(String title);
}
