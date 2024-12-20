package com.sarapio.votacao_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.dtos.AssociateDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;

@ExtendWith(MockitoExtension.class)
public class AssociateServiceTest {

    @Mock
    private AssociateRepository associateRepository;

    @InjectMocks
    private AssociateService associateService;

    @Test
    @DisplayName("Create associate with success")
    void createAssociate() {
        AssociateDTO associateDTO = new AssociateDTO("000.000.000-12", "Larissa");

        Associate associate = new Associate(associateDTO);

        when(associateRepository.save(any(Associate.class))).thenReturn(associate);

        Associate result = associateService.createAssociate(associateDTO);

        assertEquals("000.000.000-12", result.getCpf()); 
        assertEquals("Larissa", result.getName());        
        verify(associateRepository).save(any(Associate.class));

    }

    @Test
    void shouldThrowExceptionWhenCpfIsNull() {
        AssociateDTO associateDTO = new AssociateDTO(null, "Name");
        
        assertThrows(IllegalArgumentException.class, () -> associateService.createAssociate(associateDTO));
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        String cpf = "12345678901";
        AssociateDTO associateDTO = new AssociateDTO(cpf, "Name");
        Mockito.when(associateRepository.findByCpf(cpf)).thenReturn(Optional.of(new Associate()));

        assertThrows(IllegalStateException.class, () -> associateService.createAssociate(associateDTO));
    }


}
