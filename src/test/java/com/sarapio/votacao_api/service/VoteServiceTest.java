package com.sarapio.votacao_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.UUID;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @Mock
    private SessionService sessionService;

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

    @Test
    @DisplayName("Should return error when session is closed")
    void shouldReturnErrorWhenSessionIsClosed() {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);

        Associate associate = new Associate();
        Session session = new Session();
        session.setDateEnd(Instant.now().toEpochMilli() - 10000); 
        session.setCountVoto(0);

        when(associateRepository.findById(associateId)).thenReturn(Optional.of(associate));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        
        String result = voteService.registerVote(associateId, sessionId, voteDTO);

        assertEquals("A sessão está encerrada e não é possível votar!", result);
        
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    @DisplayName("Should return error when associate has already voted")
    void shouldReturnErrorWhenAssociateHasAlreadyVoted() {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);

        Associate associate = new Associate();
        Session session = new Session();
        session.setDateEnd(Instant.now().toEpochMilli() + 10000); 
        session.setCountVoto(0);

        Vote existingVote = new Vote();
        existingVote.setAssociate(associate);
        existingVote.setSession(session);

        when(associateRepository.findById(associateId)).thenReturn(Optional.of(associate));
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(voteRepository.findByAssociateAndSession(associate, session)).thenReturn(existingVote); 

        String result = voteService.registerVote(associateId, sessionId, voteDTO);

        assertEquals(VoteMessages.ALREADY_VOTED, result);
        
        verify(voteRepository, never()).save(any(Vote.class));
    }

    @Test
    @DisplayName("Register vote successfully")
    void sessionDontSearch() {
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
        
        verify(voteRepository, times(1)).save(any(Vote.class));

        assertEquals(1, session.getCountVoto());
        
    }

    @Test
    @DisplayName("Associate is not found")
    void shouldThrowExceptionWhenAssociateIsNotFound() {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);

        when(associateRepository.findById(associateId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            voteService.registerVote(associateId, sessionId, voteDTO);
        });

        assertEquals("User not found", exception.getMessage());

        verify(voteRepository, never()).save(any(Vote.class));

    }

    @Test
    @DisplayName("Return all sessions")
    void allSessions() {
      
    }


    private static class VoteMessages {
        public static final String VOTE_SUCCESS = "Voto registrado com sucesso!";
        public static final String ALREADY_VOTED = "Associado já votou!";
    }

}
