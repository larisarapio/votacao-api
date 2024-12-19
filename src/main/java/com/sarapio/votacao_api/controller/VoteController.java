package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.dtos.VoteDTO;
import com.sarapio.votacao_api.service.VoteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/register/{associateId}/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerVote(@PathVariable UUID associateId, @PathVariable Long sessionId, @RequestBody VoteDTO data) {
        String result = voteService.registerVote(associateId, sessionId, data);
        
        if (result.equals("Você já votou nesta sessão!")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result); 
        } else {
            return ResponseEntity.ok(result); 
        }
    }

    @GetMapping("/result")
    public ResponseEntity<List<Session>> getResults() {
        List<Session> result = voteService.getAllSessions();
        return ResponseEntity.ok(result);
    }

}
