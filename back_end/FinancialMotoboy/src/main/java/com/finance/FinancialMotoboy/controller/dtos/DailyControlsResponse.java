package com.finance.FinancialMotoboy.controller.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record DailyControlsResponse(
    BigDecimal faturamento,
    BigDecimal gasolina,
    BigDecimal comida,
    String observation,
    LocalDateTime localDateTime,
    BigDecimal proft
) {
}


