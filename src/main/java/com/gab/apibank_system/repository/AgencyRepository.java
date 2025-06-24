package com.gab.apibank_system.repository;

import com.gab.apibank_system.domain.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AgencyRepository extends JpaRepository<Agency, UUID> {
}
