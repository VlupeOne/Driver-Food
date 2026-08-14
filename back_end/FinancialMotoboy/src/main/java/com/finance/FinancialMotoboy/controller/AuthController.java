package com.finance.FinancialMotoboy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserRequest;
import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.controller.dtos.LoginRequest;
import com.finance.FinancialMotoboy.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<DefaultUserResponse> register(@RequestBody DefaultUserRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(
            service.register(request, response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        service.authenticate(loginRequest, response);
        return ResponseEntity.ok().build();
    }

}
