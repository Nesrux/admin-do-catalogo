package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(Category category, ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        if (this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
