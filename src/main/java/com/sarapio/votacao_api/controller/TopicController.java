package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.TopicDTO;
import com.sarapio.votacao_api.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping()
    public ResponseEntity<Topic> createTopic(@RequestBody TopicDTO data) {
        Topic newTopic = topicService.createTopic(data);

        return new ResponseEntity<>(newTopic, HttpStatus.CREATED);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Topic>> listTopics() {
        List<Topic> topics = this.topicService.listTopics();
        return new ResponseEntity<>(topics, HttpStatus.OK);
    }

}
