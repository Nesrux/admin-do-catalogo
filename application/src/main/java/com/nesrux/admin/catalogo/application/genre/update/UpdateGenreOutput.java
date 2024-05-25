package com.nesrux.admin.catalogo.application.genre.update;

import com.nesrux.admin.catalogo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}