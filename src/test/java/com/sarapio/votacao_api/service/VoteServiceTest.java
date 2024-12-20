package com.sarapio.votacao_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.vote.Vote;
import com.sarapio.votacao_api.domain.vote.VoteEnum;
import com.sarapio.votacao_api.dtos.VoteDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.VoteRepository;

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private AssociateRepository associateRepository;
    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    @DisplayName("Should register vote successfully")
    void shouldRegisterVoteSuccessfully() {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);

        Associate associate = new Associate();
        Session session = new Session();
        session.setDateEnd(Instant.now().toEpochMilli() + 10000); 
        session.setCountVoto(0);

        when(associateRepository.findById(associateId)).thenReturn(Optional.of(associate));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(voteRepository.findByAssociateAndSession(associate, session)).thenReturn(null);

        String result = voteService.registerVote(associateId, sessionId, voteDTO);

        assertEquals(VoteMessages.VOTE_SUCCESS, result);
        verify(voteRepository).save(any(Vote.class));
        assertEquals(1, session.getCountVoto());
    }

    private static class VoteMessages {
        public static final String ALREADY_VOTED = "Você já votou nesta sessão!";
        public static final String VOTE_SUCCESS = "Voto registrado com sucesso!";
    }

}
