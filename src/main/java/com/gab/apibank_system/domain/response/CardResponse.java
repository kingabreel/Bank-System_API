package com.gab.apibank_system.domain.response;

import com.gab.apibank_system.domain.model.Card;

public record CardResponse(double limit, String validDate, String number, String cvv, String owner, Card.CardType cardType) {
}
