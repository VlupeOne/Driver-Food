package com.finance.FinancialMotoboy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finance.FinancialMotoboy.entities.DailyControls;

public interface DailyControlsRepository extends JpaRepository<DailyControls, Long>{
    
}
