package com.example.financialmotoboy.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.financialmotoboy.entity.Motoboy;

public interface MotoboyRepository extends JpaRepository<Motoboy, UUID> {
    Optional<Motoboy> findByEmail(String email);
}
