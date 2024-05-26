package com.nesrux.admin.catalogo.application.genre.delete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;

public class DeleteGenreUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;
    @Mock
    private GenreGateway gateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(useCase);
    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);
        final var expectedId = aGenre.getId();

        doNothing()
                .when(gateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAnIvalidGenreId_whenCallsDeleteGenre_ShouldBeOk() {
        // given
        final var expectedId = GenreID.from("12345");

        doNothing()
                .when(gateway).deleteById(any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);

    }

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrows_ShouldReciveException() {
        // given
        final var expectedId = GenreID.from("12345");

        doThrow(new IllegalStateException("Gateway error"))
                .when(gateway).deleteById(any());

        // when
        Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(expectedId);

    }

}
