package com.finance.FinancialMotoboy.controller.dtos;

public record AddressResponse(
    String cep,
    String logradouro,
    String complemento,
    String bairro,
    String localidade,
    String uf
) {}
