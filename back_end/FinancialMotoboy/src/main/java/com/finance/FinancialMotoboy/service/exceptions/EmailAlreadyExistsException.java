package com.finance.FinancialMotoboy.service.exceptions;

public class EmailAlreadyExistsException extends AttributeAlreadyExistsException {

    public EmailAlreadyExistsException() {
        super("email");
    }
}
