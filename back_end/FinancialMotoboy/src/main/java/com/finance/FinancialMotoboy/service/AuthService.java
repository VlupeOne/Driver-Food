package com.finance.FinancialMotoboy.service;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserRequest;
import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.controller.dtos.LoginRequest;
import com.finance.FinancialMotoboy.service.Register.UserRegisterHandler;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    private final List<UserRegisterHandler> handlers;
    private final UserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtService jwtService;
    private final CookieService cookieService;

    public AuthService(List<UserRegisterHandler> handlers, UserService userService, AuthenticationManagerBuilder authenticationManagerBuilder,
        CookieService cookieService, JwtService jwtService) {
            this.userService = userService;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
            this.cookieService = cookieService;
            this.jwtService = jwtService;
            this.handlers = handlers;
    }
    
    public DefaultUserResponse register(DefaultUserRequest request, HttpServletResponse response) {
        userService.verifyAttributes(
            request.email(),
            request.cpf()
        );

        DefaultUserResponse user =
            handlers.stream()
                .filter(handler -> handler.supports(request))
                .findFirst()
                .orElseThrow(() ->
                    new IllegalArgumentException("Tipo de usuário inválido")
                )
                .register(request);

        authenticate(
            new LoginRequest(
                request.email(),
                request.password()
            ),
            response
        );

        return user;
    }

    public void authenticate(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.email(),
                    loginRequest.password());

            Authentication authResult =
                    authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            System.out.println("Autenticado!");

            String jwt = jwtService.generateToken(authResult);
            cookieService.addHttpOnlyCookie("jwt", jwt, 7 * 24 * 60 * 60, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
