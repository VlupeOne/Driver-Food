package com.example.financialmotoboy.service;

import com.example.financialmotoboy.dto.AuthenticationResponse;
import com.example.financialmotoboy.dto.LoginRequest;
import com.example.financialmotoboy.dto.RegisterRequest;

public interface AuthService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);

}