package com.nesrux.admin.catalogo.application.genre.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
public class DeleteGenreUseCaseIT {
    @Autowired
    private DefaultDeleteGenreUseCase useCase;
    @Autowired
    private GenreGateway gateway;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        Assertions.assertEquals(0, genreRepository.count());
        final var aGenre = gateway.create(Genre.newGenre("Ação", true));
        final var expectedId = aGenre.getId();
     
        Assertions.assertEquals(1, genreRepository.count());
        // when

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnIvalidGenreId_whenCallsDeleteGenre_ShouldBeOk() {
        // given
        Assertions.assertEquals(0, genreRepository.count());
        final var expectedId = GenreID.from("12345");

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(0, genreRepository.count());
    }
}
