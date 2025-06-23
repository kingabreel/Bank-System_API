package com.gab.apibank_system.domain.request;

public record UserRequest(String firstName,
                          String lastName,
                          String email,
                          String password,
                          String cellphone) {
}
