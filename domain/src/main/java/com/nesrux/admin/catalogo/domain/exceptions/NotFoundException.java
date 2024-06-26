package com.nesrux.admin.catalogo.domain.exceptions;

import com.nesrux.admin.catalogo.domain.AggregateRoot;
import com.nesrux.admin.catalogo.domain.Identifier;
import com.nesrux.admin.catalogo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {
    protected NotFoundException(String aMessage, List<Error> errors) {
        super(aMessage, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id) {

        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(),
                id.getValue());

        return new NotFoundException(anError, Collections.emptyList());
    }
}
