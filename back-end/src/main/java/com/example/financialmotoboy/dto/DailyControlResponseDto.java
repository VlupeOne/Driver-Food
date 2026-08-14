package com.example.financialmotoboy.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DailyControlResponseDto {

    private Long id;
    private BigDecimal faturamento;
    private BigDecimal gasolina;
    private BigDecimal comida;
    private List<ExtraExpenseDto> extras;
    private String observation;
    private LocalDate date;
    private LocalDateTime recordedAt;
    private BigDecimal profit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(BigDecimal faturamento) {
        this.faturamento = faturamento;
    }

    public BigDecimal getGasolina() {
        return gasolina;
    }

    public void setGasolina(BigDecimal gasolina) {
        this.gasolina = gasolina;
    }

    public BigDecimal getComida() {
        return comida;
    }

    public void setComida(BigDecimal comida) {
        this.comida = comida;
    }

    public List<ExtraExpenseDto> getExtras() {
        return extras;
    }

    public void setExtras(List<ExtraExpenseDto> extras) {
        this.extras = extras;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }
}
