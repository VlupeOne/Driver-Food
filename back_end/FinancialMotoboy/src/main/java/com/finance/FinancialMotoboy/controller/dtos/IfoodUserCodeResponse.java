package com.finance.FinancialMotoboy.controller.dtos;

public record IfoodUserCodeResponse(
        String userCode,
        String authorizationCodeVerifier,
        String verificationUrl,
        String verificationUrlComplete,
        Integer expiresIn
) {
}
