package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.domain.Topic;
import com.sarapio.votacao_api.dtos.AssociateDTO;
import com.sarapio.votacao_api.dtos.TopicDTO;
import com.sarapio.votacao_api.service.AssociateService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/associate")
public class AssociateController {

    private final AssociateService associateService;

    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping()
    public ResponseEntity<Associate> createAssociate(@RequestBody AssociateDTO data) {
        Associate associate = associateService.createAssociate(data);
        return new ResponseEntity<>(associate, HttpStatus.CREATED);

    }

}