package com.example.financialmotoboy.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyControlRequestDto {

    @NotNull
    @PositiveOrZero
    private BigDecimal faturamento;

    @PositiveOrZero
    private BigDecimal gasolina;

    @PositiveOrZero
    private BigDecimal comida;

    @Valid
    private List<ExtraExpenseDto> extras = new ArrayList<>();

    private String observation;

    private LocalDate date;

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
}
