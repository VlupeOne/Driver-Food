package com.finance.FinancialMotoboy.controller.dtos;

public record MotoboyResponse(
    String name,
    String email,
    String motorcycle,
    String plate,
    AddressResponse addresResponse,
    String role
)implements DefaultUserResponse {
}
