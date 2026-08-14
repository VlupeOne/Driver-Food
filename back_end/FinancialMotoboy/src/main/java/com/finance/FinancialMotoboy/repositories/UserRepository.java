package com.finance.FinancialMotoboy.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.FinancialMotoboy.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<User> findByEmail(String email);
}
