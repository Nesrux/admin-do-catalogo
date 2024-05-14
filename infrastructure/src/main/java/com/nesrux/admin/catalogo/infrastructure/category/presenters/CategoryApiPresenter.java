package com.nesrux.admin.catalogo.infrastructure.category.presenters;

import com.nesrux.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {
    static CategoryApiOutput present(final CategoryOutput out) {
        return new CategoryApiOutput(
                out.id().getValue(),
                out.name(),
                out.description(),
                out.isActive(),
                out.createdAt(),
                out.UpdatedAt(),
                out.deletedAt());
    }
}
