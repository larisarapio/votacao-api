package com.sarapio.votacao_api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sarapio.votacao_api.dtos.TopicDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "topic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false)
    @JsonProperty("title")
    private String title;

    @Column(nullable = false)
    @JsonProperty("description")
    private String description;

    public Topic(TopicDTO data) {
        this.title = data.title();
        this.description = data.description();
    }
    
}
