package com.gab.apibank_system.domain.dto;

import java.util.UUID;

public record UserDTO(UUID id, String firstName, String lastName, String email, String password, String cellphone, AddressDTO address) {
}
