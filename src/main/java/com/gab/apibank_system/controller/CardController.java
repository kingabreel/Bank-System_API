package com.gab.apibank_system.controller;

import com.gab.apibank_system.domain.model.Card;
import com.gab.apibank_system.domain.model.User;
import com.gab.apibank_system.domain.model.Wallet;
import com.gab.apibank_system.domain.request.CardPaymentRequest;
import com.gab.apibank_system.domain.response.CardResponse;
import com.gab.apibank_system.domain.response.Response;
import com.gab.apibank_system.repository.CardRepository;
import com.gab.apibank_system.service.CardService;
import com.gab.apibank_system.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/v1/card")
public class CardController {

    private final CardRepository cardRepository;
    private final TransactionService transactionService;

    public CardController(CardRepository cardRepository, TransactionService transactionService) {
        this.cardRepository = cardRepository;
        this.transactionService = transactionService;
    }

    @PostMapping("/{cardType}")
    public ResponseEntity<Response<CardResponse>> createCard(@PathVariable String cardType) {
        Response<CardResponse> response = new Response<>();

        try {
            User user = transactionService.getCurrentUser();

            Card card = new Card();

            if(cardType.equalsIgnoreCase("credit")) {
                card.setCardType(Card.CardType.CREDIT_CARD);
            } else if(cardType.equalsIgnoreCase("debit")) {
                card.setCardType(Card.CardType.DEBIT_CARD);
            } else if(cardType.equalsIgnoreCase("virtual")) {
                card.setCardType(Card.CardType.VIRTUAL_CARD);
            } else {
                throw new IllegalArgumentException("Invalid card type");
            }

            Wallet wallet = user.getAccount().getWallet();

            card.setCardLimit(BigDecimal.valueOf(300));
            card.setWallet(wallet);
            card.setCardNumber(CardService.generateRandomCardNumber());
            card.setCvv(CardService.generateRandomCVV());
            card.setValid(CardService.generateRandomValidDate());
            card.setName(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase());

            wallet.addCard(card);

            Card savedCard = cardRepository.save(card);
            CardResponse cardResponse = new CardResponse(savedCard.getCardLimit().doubleValue(), savedCard.getValid(), savedCard.getCardNumber(), savedCard.getCvv(), savedCard.getName(), savedCard.getCardType());

            response.setSuccess(cardResponse, "Card added to your account", "201");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            if (e.getMessage().equals("Invalid card type")) {
                response.setError("invalid card type. Endpoint should be credit | debit | virtual", "400");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if (e.getMessage().equals("User is not authenticated")) {
                response.setError("User is not authenticated, please login again", "401");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            response.setError("An error has occured", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<Response<CardResponse>> payment(@RequestBody CardPaymentRequest cardPaymentRequest) {
        Response<CardResponse> response = new Response<>();

        try {
            User user = transactionService.getCurrentUser();
            Wallet wallet = user.getAccount().getWallet();
            Card card = wallet.getCard(UUID.fromString(cardPaymentRequest.cardId()));

            card.payment(card, cardPaymentRequest);

            this.cardRepository.save(card);

            if (cardPaymentRequest.installments() == 1 && card.getCardType() == Card.CardType.DEBIT_CARD) {
                this.transactionService.transferMoney(UUID.fromString(cardPaymentRequest.receiverId()), BigDecimal.valueOf(cardPaymentRequest.payment()));
            }

            CardResponse cardResponse = new CardResponse(card.getCardLimit().doubleValue(), card.getValid(), card.getCardNumber(), card.getCvv(), card.getName(), card.getCardType());

            response.setSuccess(cardResponse, "Payed", "201");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setError("An error has occured", "500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
