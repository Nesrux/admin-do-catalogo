package com.nesrux.admin.catalogo.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    ValidationHandler validate(Validation aValidation);

    List<Error> getErros();

    default boolean hasError() {
        return getErros() != null && !getErros().isEmpty();
    }

    public interface Validation {
        void validate();
    }

}
