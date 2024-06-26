package com.nesrux.admin.catalogo.application.genre.retrive.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;

public class GetGenreByIdUsecaseTest extends UseCaseTest {
    @Mock
    private GenreGateway genreGateway;
    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAvalidId_whenCallsGetGenre_shouldREturnGenre() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456"));
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        when(genreGateway.findById(any())).thenReturn(Optional.of(aGenre));

        // when
        final var actualGenre = useCase.execute(expectedId.getValue());
        // then

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());

        verify(genreGateway, times(0)).deleteById(expectedId);

    }

    @Test
    public void givenAvalidId_whenCallsGetGenreAndDoesNotExistis_shouldREturnNotfound() {
        // given
        final var expectedErrorMessage = "Genre with ID 1234 was not found";
        final var expectedId = GenreID.from("1234");

        when(genreGateway.findById(expectedId)).thenReturn(Optional.empty());

        // when
        final var actualExceptions = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));
        // then
        Assertions.assertEquals(expectedErrorMessage, actualExceptions.getMessage());

    }



}
