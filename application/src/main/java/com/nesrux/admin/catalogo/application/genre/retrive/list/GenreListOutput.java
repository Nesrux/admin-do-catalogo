package com.nesrux.admin.catalogo.application.genre.retrive.list;

import java.time.Instant;
import java.util.List;

import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.Genre;

public record GenreListOutput(
                String id,
                String name,
                boolean isActive,
                List<String> categories,
                Instant createdAt,
                Instant deletedAt) {

        public static GenreListOutput from(final Genre aGenre) {
                return new GenreListOutput(
                                aGenre.getId().getValue(),
                                aGenre.getName(),
                                aGenre.isActive(),
                                aGenre.getCategories().stream()
                                                .map(CategoryID::getValue).toList(),
                                aGenre.getCreatedAt(),
                                aGenre.getDeletedAt());
        }
}
