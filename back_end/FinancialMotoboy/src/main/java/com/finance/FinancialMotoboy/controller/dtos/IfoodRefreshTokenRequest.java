package com.finance.FinancialMotoboy.controller.dtos;

import jakarta.validation.constraints.NotBlank;

public record IfoodRefreshTokenRequest(
        @NotBlank(message = "refreshToken é obrigatório")
        String refreshToken
) {
}
