package com.example.financialmotoboy.service;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.financialmotoboy.dto.AuthenticationResponse;
import com.example.financialmotoboy.dto.LoginRequest;
import com.example.financialmotoboy.dto.RegisterRequest;
import com.example.financialmotoboy.entity.Motoboy;
import com.example.financialmotoboy.repository.MotoboyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MotoboyRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        if (repository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }

        Motoboy motoboy = new Motoboy();

        motoboy.setName(request.name());
        motoboy.setEmail(request.email());
        motoboy.setCpf(request.cpf());
        motoboy.setBirthDate(request.birthDate());
        motoboy.setPassword(passwordEncoder.encode(request.password()));

        repository.save(motoboy);

        String jwt = jwtService.generateToken(motoboy);

        return new AuthenticationResponse(jwt);
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        Motoboy motoboy = repository.findByEmail(request.email())
                .orElseThrow();

        String jwt = jwtService.generateToken(motoboy);

        return new AuthenticationResponse(jwt);
    }
}