package com.nesrux.admin.catalogo.infrastructure.configuration.usecase;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nesrux.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.create.DefaultCreateGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.delete.DefaultDeleteGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.retrive.list.DefaultListGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.retrive.list.ListGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.update.DefaultUpdateGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;

@Configuration
public class GenreUseCaseConfig {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public GenreUseCaseConfig(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public UpdateGenreUseCase UpdateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

}
