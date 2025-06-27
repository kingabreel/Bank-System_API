package com.gab.apibank_system.controller;

import com.gab.apibank_system.domain.response.Response;
import com.gab.apibank_system.domain.response.TransactionResponse;
import com.gab.apibank_system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<Response<TransactionResponse>> getAccountInfo() {
        Response<TransactionResponse> finalResponse = new Response<>();

        try {
            finalResponse.setSuccess(transactionService.getWalletInfo(), "Account information", "200");
            return ResponseEntity.ok(finalResponse);
        } catch (Exception e) {
            finalResponse.setError(e.getMessage(), "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(finalResponse);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Response<TransactionResponse>> addMoney(@RequestParam BigDecimal amount) {
        Response<TransactionResponse> response = new Response<>();
        try {
            response.setSuccess(transactionService.addMoney(amount), "Money added", "200");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError(e.getMessage(), "400");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Response<TransactionResponse>> withdrawMoney(@RequestParam BigDecimal amount) {
        Response<TransactionResponse> response = new Response<>();
        try {
            response.setSuccess(transactionService.withdrawMoney(amount), "Money withdrawn", "200");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError(e.getMessage(), "400");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<Response<TransactionResponse>> transferMoney(
            @RequestParam UUID toUserId,
            @RequestParam BigDecimal amount) {
        Response<TransactionResponse> response = new Response<>();
        try {
            response.setSuccess(transactionService.transferMoney(toUserId, amount), "Transfer completed", "200");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError(e.getMessage(), "400");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
