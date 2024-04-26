package com.nesrux.admin.catalogo.domain.exceptions;

import com.nesrux.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {
    private final List<Error> errors;

    public DomainException(final List<Error> errors) {
        super("", null, true, false); // <- ajuda na performance!
        this.errors = errors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException(anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
