package com.gab.apibank_system.controller;

import com.gab.apibank_system.domain.model.Account;
import com.gab.apibank_system.domain.model.Pix;
import com.gab.apibank_system.domain.model.PixKey;
import com.gab.apibank_system.domain.request.PixRequest;
import com.gab.apibank_system.domain.request.PixTransactionRequest;
import com.gab.apibank_system.domain.response.PixTransactionResponse;
import com.gab.apibank_system.domain.response.Response;
import com.gab.apibank_system.repository.AccountRepository;
import com.gab.apibank_system.service.PixService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pix")
public class PixController {

    private final PixService pixService;
    private final AccountRepository accountRepository;

    public PixController(PixService pixService, AccountRepository accountRepository) {
        this.pixService = pixService;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public ResponseEntity<Response<List<PixKey>>> getPix() {
        Response<List<PixKey>>  response = new Response<>();

        try {
            Account user = pixService.getUserCurrent().getAccount();

            List<PixKey> pix = pixService.getPixKeys(user);
            response.setSuccess(pix, "Keys retrieved", "200");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError("An error has occurred", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Response<Pix>> addPixKey(@RequestBody PixRequest pix) {
        Response<Pix>  response = new Response<>();

        try {
            Account user = pixService.getUserCurrent().getAccount();

            Account account = pixService.addPix(pix, user);

            accountRepository.save(account);

            response.setSuccess(account.getPix(), "Key created", "201");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setError("An error has occurred", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/transaction")
    public ResponseEntity<Response<PixTransactionResponse>> transaction(@RequestBody PixTransactionRequest pix) {
        Response<PixTransactionResponse>  response = new Response<>();

        try {
            Account user = pixService.getUserCurrent().getAccount();

            PixTransactionResponse pixTransaction = pixService.transaction(pix, user);
            response.setSuccess(pixTransaction, "Keys retrieved", "200");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setError("An error has occurred", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
