package com.gab.apibank_system.repository;

import com.gab.apibank_system.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
}
