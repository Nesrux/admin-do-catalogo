package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    protected CategoryValidator(ValidationHandler aHandler, Category category) {
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {

    }
}
