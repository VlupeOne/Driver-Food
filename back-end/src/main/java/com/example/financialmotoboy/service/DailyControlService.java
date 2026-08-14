package com.example.financialmotoboy.service;

import com.example.financialmotoboy.dto.DashboardSummaryDto;
import com.example.financialmotoboy.dto.DailyControlRequestDto;
import com.example.financialmotoboy.dto.DailyControlResponseDto;
import com.example.financialmotoboy.dto.ExtraExpenseDto;
import com.example.financialmotoboy.entity.DailyControl;
import com.example.financialmotoboy.entity.ExtraExpense;
import com.example.financialmotoboy.repository.DailyControlRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DailyControlService {

    private final DailyControlRepository dailyControlRepository;

    public DailyControlService(DailyControlRepository dailyControlRepository) {
        this.dailyControlRepository = dailyControlRepository;
    }

    public List<DailyControlResponseDto> findAll() {
        return dailyControlRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DailyControlResponseDto create(DailyControlRequestDto request) {
        validateRequestValues(request);

        DailyControl entity = new DailyControl();
        entity.setFaturamento(request.getFaturamento());
        entity.setGasolina(request.getGasolina() != null ? request.getGasolina() : BigDecimal.ZERO);
        entity.setComida(request.getComida() != null ? request.getComida() : BigDecimal.ZERO);
        entity.setObservation(request.getObservation());
        entity.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        entity.setRecordedAt(LocalDateTime.now());
        entity.setExtras(mapExtras(request.getExtras()));

        DailyControl saved = dailyControlRepository.save(entity);
        return mapToDto(saved);
    }

    public List<DailyControlResponseDto> findRecent(int limit) {
        return dailyControlRepository.findAllByOrderByDateDesc(PageRequest.of(0, limit)).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public DashboardSummaryDto getSummary() {
        List<DailyControl> records = dailyControlRepository.findAll();

        BigDecimal totalRevenue = records.stream()
                .map(control -> Objects.requireNonNull(control.getFaturamento()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalGasoline = records.stream()
                .map(control -> control.getGasolina() != null ? control.getGasolina() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalFood = records.stream()
                .map(control -> control.getComida() != null ? control.getComida() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalExtras = records.stream()
                .flatMap(control -> control.getExtras().stream())
                .map(extra -> Objects.requireNonNull(extra.getAmount()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalExpenses = totalGasoline.add(totalFood).add(totalExtras);
        BigDecimal profit = totalRevenue.subtract(totalExpenses);
        BigDecimal averageTicket = records.isEmpty() ? BigDecimal.ZERO : totalRevenue.divide(BigDecimal.valueOf(records.size()), 2, RoundingMode.HALF_UP);

        DashboardSummaryDto summary = new DashboardSummaryDto();
        summary.setRevenue(totalRevenue);
        summary.setExpenses(totalExpenses);
        summary.setProfit(profit);
        summary.setAverageTicket(averageTicket);
        summary.setDeliveriesCount(records.size());
        return summary;
    }

    private void validateRequestValues(DailyControlRequestDto request) {
        if (request.getFaturamento() == null || request.getFaturamento().signum() < 0) {
            throw new BadRequestException("Faturamento é obrigatório e não pode ser negativo");
        }
        if (request.getGasolina() != null && request.getGasolina().signum() < 0) {
            throw new BadRequestException("Gasolina não pode ser negativa");
        }
        if (request.getComida() != null && request.getComida().signum() < 0) {
            throw new BadRequestException("Comida não pode ser negativa");
        }
        if (request.getExtras() != null) {
            boolean anyNegative = request.getExtras().stream()
                    .anyMatch(extra -> extra.getAmount() == null || extra.getAmount().signum() < 0);
            if (anyNegative) {
                throw new BadRequestException("Valores de extras não podem ser negativos");
            }
        }
    }

    private List<ExtraExpense> mapExtras(List<ExtraExpenseDto> extras) {
        return extras == null ? List.of() : extras.stream()
                .map(dto -> new ExtraExpense(dto.getDescription(), dto.getAmount()))
                .collect(Collectors.toList());
    }

    private DailyControlResponseDto mapToDto(DailyControl entity) {
        DailyControlResponseDto dto = new DailyControlResponseDto();
        dto.setId(entity.getId());
        dto.setFaturamento(entity.getFaturamento());
        dto.setGasolina(entity.getGasolina());
        dto.setComida(entity.getComida());
        dto.setObservation(entity.getObservation());
        dto.setDate(entity.getDate());
        dto.setRecordedAt(entity.getRecordedAt());
        dto.setExtras(entity.getExtras().stream()
                .map(extra -> {
                    ExtraExpenseDto dtoExtra = new ExtraExpenseDto();
                    dtoExtra.setDescription(extra.getDescription());
                    dtoExtra.setAmount(extra.getAmount());
                    return dtoExtra;
                })
                .collect(Collectors.toList()));
        dto.setProfit(calculateProfit(entity));
        return dto;
    }

    private BigDecimal calculateProfit(DailyControl control) {
        BigDecimal gasoline = control.getGasolina() != null ? control.getGasolina() : BigDecimal.ZERO;
        BigDecimal food = control.getComida() != null ? control.getComida() : BigDecimal.ZERO;
        BigDecimal extras = control.getExtras().stream()
                .map(extra -> Objects.requireNonNull(extra.getAmount()))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        return control.getFaturamento().subtract(gasoline.add(food).add(extras));
    }
}
