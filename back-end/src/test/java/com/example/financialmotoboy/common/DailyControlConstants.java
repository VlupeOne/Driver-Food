package com.example.financialmotoboy.common;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.example.financialmotoboy.dto.DailyControlRequestDto;
import com.example.financialmotoboy.dto.ExtraExpenseDto;
import com.example.financialmotoboy.entity.DailyControl;
import com.example.financialmotoboy.entity.ExtraExpense;

public final class DailyControlConstants {

    private DailyControlConstants() {}

    /* =========================
       REQUESTS
       ========================= */

    public static DailyControlRequestDto validRequest() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("120.00"));
        dto.setGasolina(new BigDecimal("20.00"));
        dto.setComida(new BigDecimal("10.00"));
        return dto;
    }

    public static DailyControlRequestDto requestWithoutExpenses() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("100.00"));
        return dto;
    }

    public static DailyControlRequestDto requestWithExtras() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("200.00"));
        dto.setGasolina(new BigDecimal("20.00"));
        dto.setComida(new BigDecimal("10.00"));
        dto.setExtras(List.of(extraValid()));
        return dto;
    }

    public static DailyControlRequestDto requestRevenueNegative() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("-1"));
        return dto;
    }

    public static DailyControlRequestDto requestGasolineNegative() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("100.00"));
        dto.setGasolina(new BigDecimal("-5.00"));
        return dto;
    }

    public static DailyControlRequestDto requestFoodNegative() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("100.00"));
        dto.setComida(new BigDecimal("-10.00"));
        return dto;
    }

    public static DailyControlRequestDto requestExtraNegative() {
        DailyControlRequestDto dto = new DailyControlRequestDto();
        dto.setFaturamento(new BigDecimal("100.00"));
        dto.setExtras(List.of(extraNegative()));
        return dto;
    }

    /* =========================
       EXTRAS
       ========================= */

    public static ExtraExpenseDto extraValid() {
        ExtraExpenseDto extra = new ExtraExpenseDto();
        extra.setDescription("Pedágio");
        extra.setAmount(new BigDecimal("15.00"));
        return extra;
    }

    public static ExtraExpenseDto extraNegative() {
        ExtraExpenseDto extra = new ExtraExpenseDto();
        extra.setDescription("Erro");
        extra.setAmount(new BigDecimal("-1"));
        return extra;
    }

    /* =========================
       ENTITIES
       ========================= */

    public static DailyControl entityBasic() {
        DailyControl control = new DailyControl();
        control.setId(1L);
        control.setFaturamento(new BigDecimal("100.00"));
        control.setGasolina(BigDecimal.ZERO);
        control.setComida(BigDecimal.ZERO);
        control.setDate(LocalDate.now());
        control.setRecordedAt(LocalDateTime.now());
        control.setExtras(List.of());
        return control;
    }

    public static DailyControl entityWithExtras() {
        DailyControl control = new DailyControl();
        control.setId(3L);
        control.setFaturamento(new BigDecimal("200.00"));
        control.setGasolina(new BigDecimal("20.00"));
        control.setComida(new BigDecimal("10.00"));
        control.setExtras(List.of(
                new ExtraExpense("Pedágio", new BigDecimal("15.00"))
        ));
        return control;
    }

    public static DailyControl entityForSummary1() {
        DailyControl c = new DailyControl();
        c.setFaturamento(new BigDecimal("200.00"));
        c.setGasolina(new BigDecimal("20.00"));
        c.setComida(new BigDecimal("10.00"));
        c.setExtras(List.of(new ExtraExpense("Pedágio", new BigDecimal("5.00"))));
        return c;
    }

    public static DailyControl entityForSummary2() {
        DailyControl c = new DailyControl();
        c.setFaturamento(new BigDecimal("150.00"));
        c.setGasolina(new BigDecimal("15.00"));
        c.setComida(new BigDecimal("5.00"));
        c.setExtras(List.of(new ExtraExpense("Estacionamento", new BigDecimal("10.00"))));
        return c;
    }
}