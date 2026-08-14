package com.finance.FinancialMotoboy.controller.dtos;

public record IfoodTokenResponse(
        String accessToken,
        String refreshToken,
        String type,
        Integer expiresIn
) {
}
