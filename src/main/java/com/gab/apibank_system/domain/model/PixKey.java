package com.gab.apibank_system.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
public class PixKey {
    private String keyType;
    private String keyValue;
}

