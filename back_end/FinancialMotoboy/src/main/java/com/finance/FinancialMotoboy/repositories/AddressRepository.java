package com.finance.FinancialMotoboy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.FinancialMotoboy.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
