package com.gab.apibank_system.repository;

import com.gab.apibank_system.domain.model.Pix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PixRepository extends JpaRepository<Pix, UUID> {

    @Query("SELECT p FROM pix p JOIN p.keys k WHERE k.keyValue = :keyValue")
    Optional<Pix> findByKeyValue(@Param("keyValue") String keyValue);
}
