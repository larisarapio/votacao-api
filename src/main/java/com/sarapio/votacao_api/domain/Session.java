package com.sarapio.votacao_api.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sarapio.votacao_api.dtos.SessionDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "session")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dateStart;

    @Column(nullable = false)
    private Long dateEnd;

    @Column(nullable = false)
    private int countVoto;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "id")
    private Topic topic;

    public Session(Long dateStart, Long dateEnd, int countVoto, Topic topic) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.countVoto = countVoto;
        this.topic = topic;
    }

    public Session(SessionDTO data) {
        this.topic = new Topic();
        this.topic.setId(data.topicId());
    }
    
}
