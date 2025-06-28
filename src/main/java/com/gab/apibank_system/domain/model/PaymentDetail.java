package com.gab.apibank_system.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class PaymentDetail {
    private String id;
    private BigDecimal amount;
    private Integer count;
    private String receiver;
}
