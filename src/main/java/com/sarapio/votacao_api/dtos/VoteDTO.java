package com.sarapio.votacao_api.dtos;

import com.sarapio.votacao_api.domain.vote.VoteEnum;

public record VoteDTO(VoteEnum value) {
}
