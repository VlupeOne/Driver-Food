package com.finance.FinancialMotoboy.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.FinancialMotoboy.entities.Motoboy;

@Repository
public interface MotoboyRepository extends JpaRepository<Motoboy, UUID> {
}
