package com.sarapio.votacao_api.domain;

import com.sarapio.votacao_api.domain.vote.Vote;
import com.sarapio.votacao_api.dtos.AssociateDTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "associate")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Associate {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "associate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vote> votes;

    public Associate(AssociateDTO data) {
        this.cpf = data.cpf();
        this.name = data.name();
    }

}