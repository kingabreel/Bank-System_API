package com.gab.apibank_system.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String accountNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    @JsonBackReference
    private Agency agency;

    @OneToOne
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private Wallet wallet;

    @OneToOne
    @JoinColumn(name = "pix_id")
    @JsonBackReference
    private Pix pix;
}
