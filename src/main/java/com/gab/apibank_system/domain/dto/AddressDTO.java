package com.gab.apibank_system.domain.dto;

import java.util.UUID;

public record AddressDTO(UUID id, String street, String district, String city, String state, String country, String zipCode) {
}
