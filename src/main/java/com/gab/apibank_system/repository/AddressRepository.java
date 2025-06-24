package com.gab.apibank_system.repository;

import com.gab.apibank_system.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
