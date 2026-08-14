package com.finance.FinancialMotoboy.controller.dtos;

import java.time.LocalDate;

public record UserResponse (
        String name,
        String email,
        String cpf,
        LocalDate birthDate
)implements DefaultUserResponse {}

