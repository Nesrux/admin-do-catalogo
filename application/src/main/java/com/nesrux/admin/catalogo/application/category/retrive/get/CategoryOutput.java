package com.nesrux.admin.catalogo.application.category.retrive.get;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(CategoryID id,
                             String name,
                             String description,
                             boolean isActive,
                             Instant createdAt,
                             Instant UpdatedAt,
                             Instant deletedAt) {

    public static CategoryOutput from(Category aCategory) {
        return new CategoryOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt());
    }
}
