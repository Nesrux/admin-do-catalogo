package com.nesrux.admin.catalogo.application.category.create;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryId;

public record CreateCategoryOutput(CategoryId id) {

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }

    public static CreateCategoryOutput from(final CategoryId anId) {
        return new CreateCategoryOutput(anId);
    }

}
