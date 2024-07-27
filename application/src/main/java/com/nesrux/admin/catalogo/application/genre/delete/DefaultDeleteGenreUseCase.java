package com.nesrux.admin.catalogo.application.genre.delete;

import java.util.Objects;

import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(GenreGateway gateway) {
        this.genreGateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(String genreId) {
        this.genreGateway.deleteById(GenreID.from(genreId));
    }

}
