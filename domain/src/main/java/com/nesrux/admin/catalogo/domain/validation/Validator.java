package com.nesrux.admin.catalogo.domain.validation;

public abstract class Validator {
    private final ValidationHandler handler;

    public abstract void validate();

    protected Validator(final ValidationHandler aHandler) {
        this.handler = aHandler;
    }

    protected ValidationHandler validationHandler() {
        return this.handler;
    }
}
