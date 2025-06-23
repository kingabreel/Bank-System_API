package com.gab.apibank_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    @PostMapping
    public ResponseEntity<String> createAccount() {

        return ResponseEntity.ok("ok");
    }
}
