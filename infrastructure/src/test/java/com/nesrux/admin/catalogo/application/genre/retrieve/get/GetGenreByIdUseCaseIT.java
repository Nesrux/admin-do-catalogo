package com.nesrux.admin.catalogo.application.genre.retrieve.get;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private GenreGateway genreGateway;
    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Test
    public void givenAvalidId_whenCallsGetGenre_shouldREturnGenre() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());
        final var aGenre = genreGateway.create(
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories));

        final var expectedId = aGenre.getId();

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

    }

    @Test
    public void givenAvalidId_whenCallsGetGenreAndDoesNotExistis_shouldREturnNotfound() {
        // given
        final var expectedErrorMessage = "Genre with ID 1234 was not found";
        final var expectedId = GenreID.from("1234");

        // when
        final var actualExceptions = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));
        // then
        Assertions.assertEquals(expectedErrorMessage, actualExceptions.getMessage());

    }

    private List<String> asString(final List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
