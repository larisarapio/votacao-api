package com.sarapio.votacao_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.SessionDTO;
import com.sarapio.votacao_api.service.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
public class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    private static final Long VALID_TOPIC_ID = 6L;
    private static final Long INVALID_TOPIC_ID = 123L;

    @Test
    @DisplayName("Should return 400 BAD REQUEST when information is invalid")
    void shouldReturnBadRequestWhenInformationIsInvalid() throws Exception {

        SessionDTO invalidSessionDTO = new SessionDTO(1L, 1L, 1L);
        String jsonContent = objectMapper.writeValueAsString(invalidSessionDTO);

        var response = mvc.perform(post("/sessions/{topicId}", INVALID_TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return 201 CREATED when information is valid")
    void shouldReturnCreatedWhenInformationIsValid() throws Exception {

        SessionDTO validSessionDTO = new SessionDTO(VALID_TOPIC_ID, 1L, 1L);
        Session mockSession = createMockSession(VALID_TOPIC_ID);

        when(sessionService.createSession(VALID_TOPIC_ID)).thenReturn(mockSession);

        mvc.perform(post("/sessions/{topicId}", VALID_TOPIC_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSessionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.dateStart").value(1734561793573L))
                .andExpect(jsonPath("$.dateEnd").value(1734577200000L))
                .andExpect(jsonPath("$.countVoto").value(0))
                .andExpect(jsonPath("$.topic.id").value(6))
                .andExpect(jsonPath("$.topic.title").value("Topic Title"))
                .andExpect(jsonPath("$.topic.description").value("Topic Description"));

        verify(sessionService, times(1)).createSession(VALID_TOPIC_ID);
    }

    private Session createMockSession(Long topicId) {
        Topic topic = new Topic(topicId, "Topic Title", "Topic Description");
        return new Session(6L, 1734561793573L, 1734577200000L, 0, topic);
    }

}
