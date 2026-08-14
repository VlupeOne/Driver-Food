package com.finance.FinancialMotoboy.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyControls {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero
    private BigDecimal faturamento;

    @PositiveOrZero
    private BigDecimal gasolina;

    @PositiveOrZero
    private BigDecimal comida;

    @PositiveOrZero
    private BigDecimal profit;

    private String observation;

    private LocalDateTime localDateTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motoboy_id")
    private Motoboy motoboy;
}

