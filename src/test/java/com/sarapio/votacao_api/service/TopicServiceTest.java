package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.TopicDTO;
import com.sarapio.votacao_api.repositories.TopicRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    @Test
    @DisplayName("Create topic with success")
    void shouldCreateTopicWhenDataIsValid() {
        TopicDTO topicDTO = new TopicDTO("Valid Title", "Valid Description");

        Topic topic = new Topic(topicDTO);
        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        Topic result = topicService.createTopic(topicDTO);

        assertNotNull(result);
        assertEquals("Valid Title", result.getTitle());
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    @DisplayName("Cannot create duplicate topics")
    void shouldThrowExceptionWhenTopicTitleIsDuplicate() {
        TopicDTO topicDTO = new TopicDTO("Duplicate Title", "Description");

        when(topicRepository.existsByTitle(topicDTO.title())).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            topicService.createTopic(topicDTO);
        });

        assertEquals("Topic with this title already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("Title or description are null")
    void shouldThrowExceptionWhenTitleOrDescriptionIsEmpty() {
        TopicDTO topicDTO = new TopicDTO("", "Valid Description");

        try {
            topicService.createTopic(topicDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Title is required!", e.getMessage());
        }

        TopicDTO invalidTopicDTO = new TopicDTO("Valid Title", "");
        try {
            topicService.createTopic(invalidTopicDTO);
        } catch (IllegalArgumentException e) {
            assertEquals("Description is required!", e.getMessage());
        }

        verify(topicRepository, never()).save(any(Topic.class));

    }

}