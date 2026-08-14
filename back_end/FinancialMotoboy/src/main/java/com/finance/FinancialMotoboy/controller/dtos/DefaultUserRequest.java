package com.finance.FinancialMotoboy.controller.dtos;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.finance.FinancialMotoboy.entities.User;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(
        value = MotoboyRegisterRequest.class,
        name = "MOTOBOY"
    ),
})
public sealed interface DefaultUserRequest permits MotoboyRegisterRequest {
    String name();
    String email();
    String cpf();
    LocalDate birthDate();
    String password();

    User buildUser(PasswordEncoder passwordEncoder);
}

