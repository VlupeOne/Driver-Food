package com.finance.FinancialMotoboy.service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finance.FinancialMotoboy.controller.dtos.DefaultUserResponse;
import com.finance.FinancialMotoboy.entities.User;
import com.finance.FinancialMotoboy.repositories.UserRepository;
import com.finance.FinancialMotoboy.service.exceptions.CpfAlreadyExistsException;
import com.finance.FinancialMotoboy.service.exceptions.EmailAlreadyExistsException;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void verifyAttributes(String email, String cpf) {
        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        if (repository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException();
        }
    }

    public DefaultUserResponse getAuthenticatedUser(Authentication authentication){
        User user = getAuthenticated(authentication);
        return user.toResponse();
    }

    public User getAuthenticated(Authentication authentication) {

        String email = authentication.getName();

        User user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        
        return user;
    }

}
