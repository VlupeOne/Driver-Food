package com.finance.FinancialMotoboy.service.exceptions;

public class CpfAlreadyExistsException extends AttributeAlreadyExistsException {

    public CpfAlreadyExistsException() {
        super("cpf");
    }
}

