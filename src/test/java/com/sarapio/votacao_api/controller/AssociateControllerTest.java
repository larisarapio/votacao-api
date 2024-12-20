package com.sarapio.votacao_api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.dtos.AssociateDTO;
import com.sarapio.votacao_api.service.AssociateService;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureJsonTesters
public class AssociateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AssociateService associateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 400 BAD REQUEST when information is invalid")
    void shouldReturnBadRequestWhenInformationIsInvalid() throws Exception {
        var response = mvc.perform(post("/associate"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @Test
    @DisplayName("Should return 201 CREATED when information is valid")
    void shouldReturnCreatedWhenInformationIsValid() throws Exception {
        AssociateDTO associateDTO = new AssociateDTO("000.000.023-42", "Larissa");
        Associate associate = new Associate(associateDTO);

        when(associateService.createAssociate(any(AssociateDTO.class))).thenReturn(associate);

        mvc.perform(post("/associate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value("000.000.023-42"))
                .andExpect(jsonPath("$.name").value("Larissa"));
    }
}
