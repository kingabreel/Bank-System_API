package com.gab.apibank_system.domain.dto;

import java.util.UUID;

public record AgencyDTO(UUID id, String name, String email, String number, AddressDTO address) {
}
