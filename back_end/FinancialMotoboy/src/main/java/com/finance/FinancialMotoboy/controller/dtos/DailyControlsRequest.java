package com.finance.FinancialMotoboy.controller.dtos;

import java.math.BigDecimal;

public record DailyControlsRequest(
    BigDecimal faturamento,
    BigDecimal gasolina,
    BigDecimal comida,
    String observation
) {}



