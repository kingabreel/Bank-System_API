package com.gab.apibank_system.service;

import com.gab.apibank_system.domain.model.Card;
import com.gab.apibank_system.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }

    public static String generateRandomCardNumber() {
        StringBuilder card = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            card.append((int) (Math.random() * 10));
        }
        return card.toString();
    }

    public static String generateRandomValidDate() {
        int month = 1 + (int)(Math.random() * 12); // 1 a 12
        int year = LocalDate.now().plusYears(1 + (int)(Math.random() * 4)).getYear() % 100;

        return String.format("%02d/%02d", month, year);
    }

    public static String generateRandomCVV() {
        int cvv = 100 + (int)(Math.random() * 900);
        return String.valueOf(cvv);
    }

}
