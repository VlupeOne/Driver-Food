package com.finance.FinancialMotoboy.controller.dtos;

import jakarta.validation.constraints.NotBlank;

public record IfoodAuthorizationCodeRequest(
        @NotBlank(message = "authorizationCode é obrigatório")
        String authorizationCode,

        @NotBlank(message = "authorizationCodeVerifier é obrigatório")
        String authorizationCodeVerifier
) {
}
