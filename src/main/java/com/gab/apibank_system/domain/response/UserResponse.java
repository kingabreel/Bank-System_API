package com.gab.apibank_system.domain.response;

import java.util.UUID;

public record UserResponse (UUID id, String firstName, String lastName, String email, String cellphone){
}
