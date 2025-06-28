package com.gab.apibank_system.domain.request;

public record CardPaymentRequest(String cardId, double payment, int installments, String receiverId) {
}
