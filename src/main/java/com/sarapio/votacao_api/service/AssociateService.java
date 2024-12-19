package com.sarapio.votacao_api.service;

import com.sarapio.votacao_api.domain.Associate;
import com.sarapio.votacao_api.dtos.AssociateDTO;
import com.sarapio.votacao_api.repositories.AssociateRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;

    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    public Associate createAssociate(AssociateDTO associateDTO) {
        validateAssociateData(associateDTO);
        
        String cpf = associateDTO.cpf();
        checkIfAssociateExists(cpf);

        return saveNewAssociate(cpf, associateDTO.name());
    }

    private void validateAssociateData(AssociateDTO associateData) {
        if (associateData.cpf() == null || associateData.cpf().isEmpty()) {
            throw new IllegalArgumentException("CPF is required!");
        }

        if (associateData.name() == null || associateData.name().isEmpty()) {
            throw new IllegalArgumentException("Name is required!");
        }
    }

    private void checkIfAssociateExists(String cpf) {
        Optional<Associate> existingAssociate = associateRepository.findByCpf(cpf);
        if (existingAssociate.isPresent()) {
            throw new IllegalStateException("Associate with this CPF is already registered!");
        }
    }

    private Associate saveNewAssociate(String cpf, String name) {
        Associate associate = new Associate();
        associate.setCpf(cpf);
        associate.setName(name);

        return associateRepository.save(associate);
    }
}
