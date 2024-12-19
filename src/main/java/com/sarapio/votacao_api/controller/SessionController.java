package com.sarapio.votacao_api.controller;

import com.sarapio.votacao_api.domain.Session;
import com.sarapio.votacao_api.dtos.SessionDTO;
import com.sarapio.votacao_api.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSession(@RequestBody SessionDTO data) {
        if (data.topicId() == null || data.topicId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Topic ID is required and must be valid");
        }

        try {
            Session session = sessionService.createSession(data);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid topic: " + e.getMessage());
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Session>> listSessions() {
        List<Session> sessions = sessionService.listSession();
        return ResponseEntity.ok(sessions);
    }


}
