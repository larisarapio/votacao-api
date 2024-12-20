package com.sarapio.votacao_api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.domain.vote.VoteEnum;
import com.sarapio.votacao_api.dtos.VoteDTO;
import com.sarapio.votacao_api.service.VoteService;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
public class VoteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VoteService voteService;


    @Test
    @DisplayName("Should register vote successfully")
    void shouldRegisterVoteSuccessfully() throws Exception {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);
        String successMessage = "Voto registrado com sucesso!";

        when(voteService.registerVote(associateId, sessionId, voteDTO)).thenReturn(successMessage);

        mvc.perform(post("/vote/register/{associateId}/{sessionId}", associateId, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(voteDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    @DisplayName("Should return conflict when vote already registered")
    void shouldReturnConflictWhenVoteAlreadyRegistered() throws Exception {
        UUID associateId = UUID.randomUUID();
        Long sessionId = 1L;
        VoteDTO voteDTO = new VoteDTO(VoteEnum.YES);
        String conflictMessage = "Você já votou nesta sessão!";

        when(voteService.registerVote(associateId, sessionId, voteDTO)).thenReturn(conflictMessage);

        mvc.perform(post("/vote/register/{associateId}/{sessionId}", associateId, sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(voteDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(conflictMessage));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
