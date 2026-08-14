package com.finance.FinancialMotoboy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.finance.FinancialMotoboy.controller.dtos.DailyControlsRequest;
import com.finance.FinancialMotoboy.controller.dtos.DailyControlsResponse;
import com.finance.FinancialMotoboy.entities.DailyControls;
import com.finance.FinancialMotoboy.entities.Motoboy;
import com.finance.FinancialMotoboy.entities.User;
import com.finance.FinancialMotoboy.repositories.DailyControlsRepository;
import com.finance.FinancialMotoboy.service.exceptions.BadRequestException;

import jakarta.transaction.Transactional;

@Service
public class DailyControlsService {

    private final DailyControlsRepository dailyControlsRepository;
    private final UserService userService;

    public DailyControlsService(DailyControlsRepository dailyControlRepository, UserService userService) {
        this.dailyControlsRepository = dailyControlRepository;
        this.userService = userService;
    }

    public List<DailyControlsResponse> findAll(Authentication authentication) {
        User user = userService.getAuthenticated(authentication);

        Motoboy motoboy = (Motoboy) user;

        return motoboy.getDailyControls()
            .stream()
            .map(this::mapToDto)
            .toList();
    }

    @Transactional
    public DailyControlsResponse create(Authentication authentication, DailyControlsRequest response) {
        User user = userService.getAuthenticated(authentication);

        Motoboy motoboy = (Motoboy) user;
        validateresponseValues(response);

        DailyControls entity = new DailyControls();

        entity.setFaturamento(response.faturamento());
        entity.setGasolina(response.gasolina() != null ? response.gasolina() : BigDecimal.ZERO);
        entity.setComida(response.comida() != null ? response.comida() : BigDecimal.ZERO);

        BigDecimal profit = response.faturamento().subtract(response.gasolina()) .subtract(response.comida());

        entity.setProfit(profit);
        entity.setObservation(response.observation());
        entity.setLocalDateTime(LocalDateTime.now());
        
        entity.setMotoboy(motoboy);
        motoboy.getDailyControls().add(entity);

        dailyControlsRepository.save(entity);
        DailyControls saved = dailyControlsRepository.save(entity);
        return mapToDto(saved);
    }

    private void validateresponseValues(DailyControlsRequest response) {
        if (response.faturamento() == null || response.faturamento().signum() < 0) {
            throw new BadRequestException("Faturamento é obrigatório e não pode ser negativo");
        }
        if (response.gasolina() != null && response.gasolina().signum() < 0) {
            throw new BadRequestException("Gasolina não pode ser negativa");
        }
        if (response.comida() != null && response.comida().signum() < 0) {
            throw new BadRequestException("Comida não pode ser negativa");
        }
    }

    private DailyControlsResponse mapToDto(DailyControls entity) {
        return new DailyControlsResponse(
            entity.getFaturamento(),
            entity.getGasolina(),
            entity.getComida(),
            entity.getObservation(),
            entity.getLocalDateTime(),
            entity.getProfit()
        );
    }

}

