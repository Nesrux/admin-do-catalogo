package com.nesrux.admin.catalogo.infrastructure.category.presenters;

import com.nesrux.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.nesrux.admin.catalogo.application.category.retrive.list.CategoryListOutput;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    static CategoryResponse present(final CategoryOutput out) {
        return new CategoryResponse(
                out.id().getValue(),
                out.name(),
                out.description(),
                out.isActive(),
                out.createdAt(),
                out.UpdatedAt(),
                out.deletedAt());
    }

    static CategoryListResponse present(final CategoryListOutput out) {
        return new CategoryListResponse(
                out.id().getValue(),
                out.name(),
                out.description(),
                out.isActive(),
                out.createdAt(),
                out.deletedAt());
    }
}
