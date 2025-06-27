package com.gab.apibank_system.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallet")
@Entity(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal balance;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<Card> cards;

    public void addCard(Card card) {
        this.cards.add(card);
        card.setWallet(this);
    }

    public void removeCard(UUID cardId) {
        Card card = this.getCard(cardId);

        if (card != null) {
            cards.remove(card);
            card.setWallet(null);
        }
    }

    protected BigDecimal getCardLimit(UUID cardId) {
        Card card = this.getCard(cardId);

        if (card != null) {
            if (card.getCardType() == Card.CardType.CREDIT_CARD) {
                return card.getCard_limit();
            } else {
                return this.getBalance();
            }
        }

        return null;
    }

    private Card getCard(UUID cardId) {
        return cards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElse(null);
    }
}
