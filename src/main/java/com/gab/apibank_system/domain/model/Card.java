package com.gab.apibank_system.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gab.apibank_system.domain.request.CardPaymentRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "card")
@Entity(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    private CardType cardType = CardType.DEBIT_CARD;

    @Builder.Default
    private BigDecimal cardLimit = BigDecimal.valueOf(300);

    @Builder.Default
    private BigDecimal invoice =  BigDecimal.ZERO;

    private String cardNumber;

    private String name;

    private String valid;

    private String cvv;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

    @ElementCollection
    @CollectionTable(name = "user_payments", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "user_uuid")
    private Map<UUID, PaymentDetail> payments = new HashMap<>();

    public void payment(Card card, CardPaymentRequest cardPaymentRequest) {
        if (card.getCardType() == CardType.CREDIT_CARD) {
            creditPayment(cardPaymentRequest.payment(), cardPaymentRequest.installments(), cardPaymentRequest.receiverId());
        } else if (card.getCardType() == CardType.DEBIT_CARD || card.getCardType() == CardType.VIRTUAL_CARD) {
            debitPayment(cardPaymentRequest.payment());
        }
    }

    public void debitPayment(double payment) {
        if (payment <= 0) {
            throw new IllegalArgumentException("Payment must be greater than 0.");
        }

        BigDecimal balance =  this.wallet.getBalance();

        if (payment > balance.doubleValue()) {
            throw new RuntimeException("You don't have  enough balance.");
        }

        this.wallet.setBalance(BigDecimal.valueOf(balance.doubleValue() - payment));
    }

    public void creditPayment(double payment, int installments, String receiverId) {
        if (payment <= 0) {
            throw new IllegalArgumentException("Payment must be greater than 0.");
        } else if(payment > this.getCardLimit().doubleValue() - invoice.doubleValue()) {
            throw new RuntimeException("You don't have enough limit to proceed with the payment");
        }

        this.setInvoice(BigDecimal.valueOf(this.getInvoice().doubleValue() + payment));
        PaymentDetail paymentDetail = new PaymentDetail();

        paymentDetail.setAmount(BigDecimal.valueOf(payment));
        paymentDetail.setCount(installments);
        paymentDetail.setReceiver(receiverId);

        payments.put(UUID.randomUUID(), paymentDetail);
    }

    public enum CardType {
        CREDIT_CARD,
        DEBIT_CARD,
        VIRTUAL_CARD
    }
}
