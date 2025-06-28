package com.gab.apibank_system.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pix")
@Entity(name = "pix")
public class Pix {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "pix_keys", joinColumns = @JoinColumn(name = "pix_id"))
    @Builder.Default
    private List<PixKey> keys = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonManagedReference
    private Account account;

    public boolean checkIfCanAddKey(String keyType) {
        if (keyType.equals("random")) {
            return true;
        }
        if (keys == null) {
            keys = new ArrayList<>();
        }

        int countNumberOfKey = 0;

        for  (PixKey pixKey : keys) {
            if (pixKey.getKeyType().equals(keyType))  {
                countNumberOfKey++;
                if (countNumberOfKey > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
