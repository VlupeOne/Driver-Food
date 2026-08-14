package com.finance.FinancialMotoboy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.FinancialMotoboy.controller.dtos.AddressResponse;
import com.finance.FinancialMotoboy.service.ViaCepService;

@RestController
@RequestMapping("/viaCep")
@PreAuthorize("hasRole('MOTOBOY')")
public class ViaCepController {
    
    private final ViaCepService viaCepService;

    public ViaCepController(ViaCepService viaCepService) {
        this.viaCepService = viaCepService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<AddressResponse> buscarEndereco(@PathVariable String cep) {

        AddressResponse endereco = viaCepService.buscarCep(cep);

        return ResponseEntity.ok(endereco);
    }
}