package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.TopicDTO;
import com.sarapio.votacao_api.repositories.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public Topic createTopic(TopicDTO data) {
        validateTopicData(data);
        validateTitleDoesNotExist(data.title());

        Topic newTopic = new Topic(data);
        this.saveTopic(newTopic);
        return newTopic;
    }

    private void validateTopicData(TopicDTO data) {
        if (isNullOrEmpty(data.title())) {
            throw new IllegalArgumentException("Title is required!");
        }
        if (isNullOrEmpty(data.description())) {
            throw new IllegalArgumentException("Description is required!");
        }
    }

    private void validateTitleDoesNotExist(String title) {
        if (topicRepository.existsByTitle(title)) {
            throw new IllegalArgumentException("Topic with this title already exists");
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public void saveTopic(Topic topic) {
        this.topicRepository.save(topic);
    }

    public List<Topic> listTopics() {
        return this.topicRepository.findAll();
    }
}
