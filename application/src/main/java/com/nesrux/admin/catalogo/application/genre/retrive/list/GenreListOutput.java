package com.nesrux.admin.catalogo.application.genre.retrive.list;

import java.time.Instant;
import java.util.List;

import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.genre.Genre;

public record GenreListOutput(
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt) {

    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream()
                        .map(CategoryId::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt());
    }
}
