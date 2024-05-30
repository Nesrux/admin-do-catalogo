package com.nesrux.admin.catalogo.application.genre.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
public class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of(filmes.getId());

        final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                asString(expectedCategories));
        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs()));

        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var aCommand = CreateGenreCommand.with(expectedName,
                expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryId>of();

        final var aCommand = CreateGenreCommand.with(expectedName,
                expectedIsActive, asString(expectedCategories));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName,
                expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateGenreCommand.with(expectedName,
                expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var filmes = CategoryId.from("456");
        final var documentarios = CategoryId.from("789");

        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series.getId(), documentarios);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var aCommand = CreateGenreCommand.with(expectName,
                expectedIsActive, asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
        Mockito.verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryId> categories) {
        return categories.stream()
                .map(CategoryId::getValue)
                .toList();
    }

}