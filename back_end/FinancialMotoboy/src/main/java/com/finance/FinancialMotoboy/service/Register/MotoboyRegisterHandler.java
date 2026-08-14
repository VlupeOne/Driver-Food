package com.finance.FinancialMotoboy.service.Register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserRequest;
import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.controller.dtos.MotoboyRegisterRequest;
import com.finance.FinancialMotoboy.entities.Motoboy;
import com.finance.FinancialMotoboy.service.MotoboyService;

@Component
public class MotoboyRegisterHandler implements UserRegisterHandler {

    private final MotoboyService motoboyService;
    private final PasswordEncoder passwordEncoder;

    public MotoboyRegisterHandler(MotoboyService motoboyService,PasswordEncoder passwordEncoder) {
        this.motoboyService = motoboyService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(DefaultUserRequest request) {
        return request instanceof MotoboyRegisterRequest;
    }

    @Override
    public DefaultUserResponse register(DefaultUserRequest request) {
        MotoboyRegisterRequest motoboyRequest = (MotoboyRegisterRequest) request;

        Motoboy motoboy = new Motoboy(
            null,
            motoboyRequest.name(),
            motoboyRequest.email(),
            passwordEncoder.encode(motoboyRequest.password()),
            motoboyRequest.cpf(),
            motoboyRequest.birthDate(),
            motoboyRequest.motorcycle(),
            motoboyRequest.plate()
        );

        Motoboy saved = motoboyService.save(motoboy);

        return saved.toResponse();
    }
    
}
