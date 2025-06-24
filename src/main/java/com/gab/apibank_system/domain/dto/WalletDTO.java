package com.gab.apibank_system.domain.dto;

import com.gab.apibank_system.domain.model.Card;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WalletDTO(UUID id, BigDecimal balance, List<Card> cards) {
}
