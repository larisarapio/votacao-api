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

    @PostMapping(value = "/{topicId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Session> createSession(
            @PathVariable Long topicId,
            @RequestBody SessionDTO sessionDTO) {

        if (!isTopicIdValid(topicId, sessionDTO)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null); 
        }

        return handleSessionCreation(topicId);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Session>> listSessions() {
        List<Session> sessions = sessionService.listSession();
        return ResponseEntity.ok(sessions);
    }

    private boolean isTopicIdValid(Long topicId, SessionDTO sessionDTO) {
        return topicId.equals(sessionDTO.topicId());
    }

    private ResponseEntity<Session> handleSessionCreation(Long topicId) {
        try {
            Session session = sessionService.createSession(topicId);
            return ResponseEntity.status(HttpStatus.CREATED).body(session);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
