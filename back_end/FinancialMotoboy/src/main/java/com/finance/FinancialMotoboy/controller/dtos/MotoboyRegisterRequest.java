package com.finance.FinancialMotoboy.controller.dtos;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.finance.FinancialMotoboy.entities.Motoboy;
import com.finance.FinancialMotoboy.entities.User;

public record MotoboyRegisterRequest(
    String name,
    String email,
    String cpf,
    LocalDate birthDate,
    String password,
    String motorcycle,
    String plate

) implements DefaultUserRequest {

    @Override
    public User buildUser(PasswordEncoder encoder) {
        return new Motoboy(
            null,
            name,
            email,
            encoder.encode(password),
            cpf,
            birthDate,
            motorcycle,
            plate
        );
    }
}
