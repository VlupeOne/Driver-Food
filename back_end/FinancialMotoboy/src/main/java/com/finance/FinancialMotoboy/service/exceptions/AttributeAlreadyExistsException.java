package com.finance.FinancialMotoboy.service.exceptions;

public abstract class AttributeAlreadyExistsException extends RuntimeException {

    private final String attribute;

    protected AttributeAlreadyExistsException(String attribute) {
        super(attribute + " já está em uso");
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
