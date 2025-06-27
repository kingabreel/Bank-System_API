package com.gab.apibank_system.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
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

    private BigDecimal card_limit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    public enum CardType {
        CREDIT_CARD,
        DEBIT_CARD,
        VIRTUAL_CARD
    }

}
