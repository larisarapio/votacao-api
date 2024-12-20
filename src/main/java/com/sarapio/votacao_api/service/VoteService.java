package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.vote.Vote;
import com.sarapio.votacao_api.dtos.VoteDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.VoteRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final AssociateRepository associateRepository;
    private final SessionRepository sessionRepository;

    public VoteService(VoteRepository voteRepository, AssociateRepository associateRepository, SessionRepository sessionRepository) {
        this.voteRepository = voteRepository;
        this.associateRepository = associateRepository;
        this.sessionRepository = sessionRepository;
    }

    public String registerVote(UUID associateId, Long sessionId, VoteDTO data) {
        Associate associate = findAssociateById(associateId);
        Session session = findSessionById(sessionId);

        if (isSessionClosed(session)) {
            return "A sessão está encerrada e não é possível votar!";
        }

        if (hasAlreadyVoted(associate, session)) {
            return VoteMessages.ALREADY_VOTED;
        }

        Vote vote = createVote(associate, session, data);
        voteRepository.save(vote);

        incrementSessionVoteCount(session);

        return VoteMessages.VOTE_SUCCESS;
    }

    private Associate findAssociateById(UUID associateId) {
        return associateRepository.findById(associateId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Session findSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
    }

    private boolean hasAlreadyVoted(Associate associate, Session session) {
        return voteRepository.findByAssociateAndSession(associate, session) != null;
    }

    private Vote createVote(Associate associate, Session session, VoteDTO data) {
        Vote vote = new Vote();
        vote.setValue(data.value());
        vote.setAssociate(associate);
        vote.setSession(session);
        return vote;
    }

    private void incrementSessionVoteCount(Session session) {
        session.setCountVoto(session.getCountVoto() + 1);
        sessionRepository.save(session);
    }

    private boolean isSessionClosed(Session session) {
        Instant currentTime = Instant.now();
        Instant sessionEndTime = Instant.ofEpochMilli(session.getDateEnd());
        return currentTime.isAfter(sessionEndTime);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    private static class VoteMessages {
        public static final String ALREADY_VOTED = "Associado já votou!";
        public static final String VOTE_SUCCESS = "Voto registrado com sucesso!";
    }
}
