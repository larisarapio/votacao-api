package com.sarapio.votacao_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.dtos.SessionDTO;
import com.sarapio.votacao_api.service.SessionService;

import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
public class SessionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private SessionService sessionService;

    @Test
    @DisplayName("Should return 400 BAD REQUEST when information is invalid")
    void shouldReturnBadRequestWhenInformationIsInvalid() throws Exception {
        Long topicId = 123L; 

        SessionDTO sessionDTO = new SessionDTO(1L, 1L, 1L);
        String jsonContent = new ObjectMapper().writeValueAsString(sessionDTO);

        var response = mvc.perform(post("/sessions/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    
}
