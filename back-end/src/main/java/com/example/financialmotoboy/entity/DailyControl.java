package com.example.financialmotoboy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "daily_controls")
public class DailyControl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal faturamento;

    @PositiveOrZero
    private BigDecimal gasolina;

    @PositiveOrZero
    private BigDecimal comida;

    @ElementCollection
    @CollectionTable(name = "daily_control_extras", joinColumns = @JoinColumn(name = "daily_control_id"))
    private List<ExtraExpense> extras = new ArrayList<>();

    private String observation;

    private LocalDate date;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime recordedAt;

    public DailyControl() {
    }

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

    public List<ExtraExpense> getExtras() {
        return extras;
    }

    public void setExtras(List<ExtraExpense> extras) {
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
}
