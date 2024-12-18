package com.sarapio.votacao_api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.TopicRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    @DisplayName("Checking whether the calculate End Date method generates the correct date for the session")
    void testCreateSessionSuccess() {
        Long topicId = 1L;
        Topic topic = new Topic(topicId, "Topic 1", "Description Topic 1");
        
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));
        when(sessionRepository.existsByTopicId(topicId)).thenReturn(false);
        
        sessionService.createSession(topicId);
        
        verify(sessionRepository).save(any(Session.class));

    }

    @Test
    @DisplayName("Topic has already been used")
    public void testCreateSessionThrowExceptionWhenTopicAlreadyUsed() {
        Long topicId = 1L;
        lenient().when(sessionRepository.existsByTopicId(topicId)).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(topicId));
    }

    @Test
    @DisplayName("Topic is not found:")
    public void testCreateSessionThrowExceptionWhenTopicNotFound() {
        Long topicId = 1L;
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(topicId));
    }

    @Test
    @DisplayName("List session return all sessions save")
    public void testListSession() {
        List<Session> expectedSessions = Arrays.asList(new Session(1L, 0L, 0, new Topic(1L, "Topic 1", "Description topic 1")));
        when(sessionRepository.findAll()).thenReturn(expectedSessions);
        
        List<Session> actualSessions = sessionService.listSession();
        
        assertEquals(expectedSessions, actualSessions);
    }

}
