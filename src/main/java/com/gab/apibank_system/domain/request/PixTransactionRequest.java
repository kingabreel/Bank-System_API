package com.gab.apibank_system.domain.request;

public record PixTransactionRequest(String pixKey, String pixType, double amount) {
}
