package com.gab.apibank_system.domain.response;

public record PixTransactionResponse(String transactionId, String key, String senderUser, String receiverUser, double amount) {
}
