package com.sarapio.votacao_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.TopicDTO;
import com.sarapio.votacao_api.service.TopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @Mock
    private TopicService topicService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 400 BAD REQUEST when information is invalid")
    void shouldReturnBadRequestWhenInformationIsInvalid() throws Exception {
        var response = mvc.perform(post("/topic"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Should return 201 CREATED when information is valid")
    void shouldReturnCreatedWhenInformationIsValid() throws Exception {

        TopicDTO topicDTO = new TopicDTO("New Topic", "Topic description");

        Topic topic = new Topic(topicDTO);

        when(topicService.createTopic(topicDTO)).thenReturn(topic);

        mvc.perform(post("/topic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(topicDTO)))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Topic"))
                .andExpect(jsonPath("$.description").value("Topic description"));
    }
}
