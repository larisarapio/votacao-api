package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final TopicRepository topicRepository;

    public SessionService(SessionRepository sessionRepository, TopicRepository topicRepository) {
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
    }

    public Session createSession(Long topicId) {
        Topic topic = findTopicById(topicId);

        Date dateEnd = calculateEndDate();

        Session session = new Session(System.currentTimeMillis(), dateEnd.getTime(), 0, topic);

        saveSession(session);

        return session;
    }

    private Date calculateEndDate() {
        LocalDate dataFinal = LocalDate.now().plusDays(1);
        return Date.from(dataFinal.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with id: " + topicId));
    }

    public void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public List<Session> listSession() {
        return sessionRepository.findAll();
    }
}
