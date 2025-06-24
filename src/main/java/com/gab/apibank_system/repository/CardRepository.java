package com.gab.apibank_system.repository;

import com.gab.apibank_system.domain.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
