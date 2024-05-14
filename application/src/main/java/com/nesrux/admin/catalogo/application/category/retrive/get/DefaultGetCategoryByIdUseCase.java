package com.nesrux.admin.catalogo.application.category.retrive.get;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        Objects.requireNonNull(this.categoryGateway = categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String AnId) {
        final var aCategoryId = CategoryId.from(AnId);
        return this.categoryGateway.findById(aCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(aCategoryId));
    }

    private Supplier<NotFoundException> notFound(final CategoryId anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }

}
