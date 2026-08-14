package com.example.financialmotoboy.dto;

import java.time.LocalDate;

public record RegisterRequest(

    String name,
    String email,
    String cpf,
    LocalDate birthDate,
    String password

) {}