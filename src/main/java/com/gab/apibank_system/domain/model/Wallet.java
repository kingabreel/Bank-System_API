package com.gab.apibank_system.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.*;

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
    @JsonManagedReference
    private List<Card> cards;

    public void addCard(Card card) {
        if (this.cards == null) {
            this.cards = new ArrayList<>();
        }
        Map<Card.CardType, Integer> cards = this.checkNumberOfCards();

        if ((cards.containsKey(card.getCardType()) && cards.get(card.getCardType()) > 1)
            && card.getCardType() != Card.CardType.VIRTUAL_CARD) {
            throw new IllegalStateException("You cannot have more than one " + card.getCardType());
        }

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
                return card.getCardLimit();
            } else {
                return this.getBalance();
            }
        }

        return null;
    }

    public Card getCard(UUID cardId) {
        return cards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElse(null);
    }

    private Map<Card.CardType, Integer>  checkNumberOfCards(){
        Map<Card.CardType, Integer> cardMap = new HashMap<>();
        int countCredit = 0;
        int countDebit = 0;
        int countVirtual = 0;
        for (Card card : cards) {
            if (card.getCardType() == Card.CardType.CREDIT_CARD) {
                countCredit++;
            } else if (card.getCardType() == Card.CardType.DEBIT_CARD) {
                countDebit++;
            } else if (card.getCardType() == Card.CardType.VIRTUAL_CARD) {
                countVirtual++;
            }
        }
        cardMap.put(Card.CardType.CREDIT_CARD, countCredit);
        cardMap.put(Card.CardType.DEBIT_CARD, countDebit);
        cardMap.put(Card.CardType.VIRTUAL_CARD, countVirtual);

        return cardMap;
    }
}
