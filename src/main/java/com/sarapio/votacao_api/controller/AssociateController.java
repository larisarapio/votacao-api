package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.dtos.AssociateDTO;
import com.sarapio.votacao_api.service.AssociateService;

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

    private static final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;

    private final AssociateService associateService;

    public AssociateController(AssociateService associateService) {
        this.associateService = associateService;
    }

    @PostMapping(produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    public ResponseEntity<Associate> createAssociate(@RequestBody AssociateDTO associateData) {
        try {
            Associate associate = associateService.createAssociate(associateData);
            return ResponseEntity.ok(associate);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);  
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); 
        }
    }
}