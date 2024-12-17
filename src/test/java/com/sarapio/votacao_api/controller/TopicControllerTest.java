package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.service.TopicService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.HttpStatus;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
class TopicControllerTest {

    @Autowired
    private MockMvc mvc;

    @InjectMocks
    private TopicService topicService;

    @Test
    @DisplayName("Error 400 when informations invalidation")
    void createTopic() throws Exception {
        var response = mvc.perform(post("/topic"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    @Test
    @DisplayName("Validated information 200")
    void createTopicCase2() throws Exception {


    }
}