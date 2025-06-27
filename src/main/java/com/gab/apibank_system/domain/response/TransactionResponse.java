package com.gab.apibank_system.domain.response;

public record TransactionResponse (String accountNumber, String agency, double amount){
}
