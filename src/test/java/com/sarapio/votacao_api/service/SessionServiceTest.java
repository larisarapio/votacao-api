package com.sarapio.votacao_api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.SessionDTO;
import com.sarapio.votacao_api.repositories.SessionRepository;
import com.sarapio.votacao_api.repositories.TopicRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Date;
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
    @DisplayName("Create session with success")
    void shouldCreateSessionWhenDataIsValid() {

        Long topicId = 1L;
        SessionDTO sessionDTO = new SessionDTO(topicId); 

        Topic topic = new Topic(topicId, "New Topic", "Description of the topic");
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(topic));

        Date dateEnd = new Date(System.currentTimeMillis() + 10000); 
        Session session = new Session(System.currentTimeMillis(), dateEnd.getTime(), 0, topic);
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.createSession(sessionDTO); 

        assertNotNull(result);
        assertEquals(session.getDateStart(), result.getDateStart());
        assertEquals(session.getDateEnd(), result.getDateEnd());
        assertEquals(session.getCountVoto(), result.getCountVoto());
        assertEquals(topic, result.getTopic());

        verify(topicRepository).findById(topicId);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    @DisplayName("Topic has already been used")
    public void testCreateSessionThrowExceptionWhenTopicAlreadyUsed() {
        Long topicId = 1L;
        SessionDTO sessionDTO = new SessionDTO(topicId); 
        lenient().when(sessionRepository.existsByTopicId(topicId)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(sessionDTO)); 
    }

    @Test
    @DisplayName("Topic is not found")
    public void testCreateSessionThrowExceptionWhenTopicNotFound() {

        Long topicId = 1L;
        SessionDTO sessionDTO = new SessionDTO(topicId); 
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty()); 
        assertThrows(IllegalArgumentException.class, () -> sessionService.createSession(sessionDTO)); 
    }


    @Test
    @DisplayName("List session returns all sessions saved")
    public void testListSession() {
        List<Session> expectedSessions = Arrays.asList(
            new Session(1L, System.currentTimeMillis(), System.currentTimeMillis() + 10000, 0, new Topic(1L, "Topic 1", "Description topic 1"))
        );
        when(sessionRepository.findAll()).thenReturn(expectedSessions); 

        List<Session> actualSessions = sessionService.listSession(); 

        assertEquals(expectedSessions, actualSessions); 
    }

}
