package com.sarapio.votacao_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.SessionDTO;
import com.sarapio.votacao_api.service.SessionService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SessionService sessionService;

    @Test
    @DisplayName("Should return 400 BAD REQUEST when information is invalid")
    void shouldReturnBadRequestWhenInformationIsInvalid() throws Exception {
        Long invalidTopicId = 999L; 

        SessionDTO invalidSessionDTO = new SessionDTO(invalidTopicId); 
        String jsonContent = objectMapper.writeValueAsString(invalidSessionDTO);

        when(sessionService.createSession(any(SessionDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid topic"));

        var response = mvc.perform(post("/sessions/create") 
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonContent))
                    .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("Should return 201 CREATED when information is valid")
    void shouldReturnCreatedWhenInformationIsValid() throws Exception {

        SessionDTO validSessionDTO = new SessionDTO(1L); 

        Session mockSession = createMockSession(1L);

        when(sessionService.createSession(any(SessionDTO.class))).thenReturn(mockSession);

        mvc.perform(post("/sessions/create") 
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validSessionDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(6))
                    .andExpect(jsonPath("$.dateStart").value(1734561793573L))
                    .andExpect(jsonPath("$.dateEnd").value(1734577200000L))
                    .andExpect(jsonPath("$.countVoto").value(0))
                    .andExpect(jsonPath("$.topic.id").value(1)) 
                    .andExpect(jsonPath("$.topic.title").value("Topic Title"))
                    .andExpect(jsonPath("$.topic.description").value("Topic Description"));

        verify(sessionService, times(1)).createSession(any(SessionDTO.class));
    }

    private Session createMockSession(Long topicId) {
        Topic topic = new Topic(topicId, "Topic Title", "Topic Description");
        return new Session(6L, 1734561793573L, 1734577200000L, 0, topic);
    }

}
