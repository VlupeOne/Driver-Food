package com.finance.FinancialMotoboy.controller.dtos;

public record LoginRequest(
    String email,
    String password
) {
}
