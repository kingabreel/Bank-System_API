package com.gab.apibank_system.domain.dto;

import java.util.UUID;

public record AccountDTO(UUID id, UserDTO owner, AgencyDTO agency, WalletDTO wallet) {
}
