package com.finance.FinancialMotoboy.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.finance.FinancialMotoboy.controller.dtos.AddressResponse;

@Service
public class ViaCepService {

    private final RestTemplate restTemplate;

    public ViaCepService() {
        this.restTemplate = new RestTemplate();
    }

    public AddressResponse buscarCep(String cep) {

        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        return restTemplate.getForObject(
                url,
                AddressResponse.class
        );
    }
}