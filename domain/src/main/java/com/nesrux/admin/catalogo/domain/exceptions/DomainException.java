package com.nesrux.admin.catalogo.domain.exceptions;

import com.nesrux.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {
    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> errors) {
        super(aMessage); // <- ajuda na performance!
        this.errors = errors;
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }

    public String firstErrorMessage() {
        return this.errors.get(0).message();
    }

    public int size() {
        return this.errors.size();
    }

}
