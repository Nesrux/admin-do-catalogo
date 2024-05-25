package com.nesrux.admin.catalogo.application.genre.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;

@ExtendWith(MockitoExtension.class)
public class UpdateGenreUseCaseTest {
        @InjectMocks
        private DefaultUpdateGenreUseCase useCase;

        @Mock
        private CategoryGateway categoryGateway;

        @Mock
        private GenreGateway genreGateway;

        @Test
        public void givenAValidCommand_whenCallsUpdateGenre_ShouldReturnGenreId() {
                // given
                final var aGenre = Genre.newGenre("Lorem ipsum", true);

                final var expectedId = aGenre.getId();
                final var expectedName = "Ação";
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var aCommand = UpdateGenreCommand.with(
                                expectedId.getValue(),
                                expectedName,
                                expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.findById(any()))
                                .thenReturn(Optional.of(Genre.with(aGenre)));

                when(genreGateway.update(any()))
                                .thenAnswer(returnsFirstArg());
                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

                verify(genreGateway, times(1))
                                .findById(eq(expectedId));
                verify(genreGateway, times(1))
                                .update(argThat(updatedGenre -> Objects.equals(expectedId, updatedGenre.getId())
                                                && Objects.equals(expectedName, updatedGenre.getName())
                                                && Objects.equals(expectedCategories, updatedGenre.getCategories())
                                                && Objects.equals(expectedIsActive, updatedGenre.isActive())
                                                && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                                                && Objects.isNull(updatedGenre.getDeletedAt())
                                                && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())));

        }

        @Test
        public void givenAValidCommandWithCategories_whenCallsUpdateGenre_ShouldReturnGenreId() {
                // given
                final var aGenre = Genre.newGenre("Lorem ipsum", true);

                final var expectedId = aGenre.getId();
                final var expectedName = "Ação";
                final var expectedIsActive = true;
                final var expectedCategories = List.of(
                                CategoryId.from("123"),
                                CategoryId.from("456"),
                                CategoryId.from("789"));

                final var aCommand = UpdateGenreCommand.with(
                                expectedId.getValue(),
                                expectedName,
                                expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.findById(any()))
                                .thenReturn(Optional.of(Genre.with(aGenre)));

                when(genreGateway.update(any()))
                                .thenAnswer(returnsFirstArg());

                when(categoryGateway.existsByIds(any()))
                                .thenReturn(expectedCategories);
                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

                verify(categoryGateway, times(1))
                                .existsByIds(eq(expectedCategories));

                verify(genreGateway, times(1))
                                .findById(eq(expectedId));

                verify(genreGateway, times(1))
                                .update(argThat(updatedGenre -> Objects.equals(expectedId, updatedGenre.getId())
                                                && Objects.equals(expectedName, updatedGenre.getName())
                                                && Objects.equals(expectedCategories, updatedGenre.getCategories())
                                                && Objects.equals(expectedIsActive, updatedGenre.isActive())
                                                && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                                                && Objects.isNull(updatedGenre.getDeletedAt())
                                                && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())));

        }

        @Test
        public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_ShouldReturnGenreId() {
                // given
                final var aGenre = Genre.newGenre("Lorem ipsum", true);

                final var expectedId = aGenre.getId();
                final var expectedName = "Ação";
                final var expectedIsActive = false;
                final var expectedCategories = List.<CategoryId>of();

                final var aCommand = UpdateGenreCommand.with(
                                expectedId.getValue(),
                                expectedName,
                                expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.findById(any()))
                                .thenReturn(Optional.of(Genre.with(aGenre)));

                when(genreGateway.update(any()))
                                .thenAnswer(returnsFirstArg());

                Assertions.assertNull(aGenre.getDeletedAt());
                Assertions.assertTrue(aGenre.isActive());
                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

                verify(genreGateway, times(1))
                                .findById(eq(expectedId));
                verify(genreGateway, times(1))
                                .update(argThat(updatedGenre -> Objects.equals(expectedId, updatedGenre.getId())
                                                && Objects.equals(expectedName, updatedGenre.getName())
                                                && Objects.equals(expectedCategories, updatedGenre.getCategories())
                                                && Objects.equals(expectedIsActive, updatedGenre.isActive())
                                                && Objects.equals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt())
                                                && Objects.nonNull(updatedGenre.getDeletedAt())
                                                && aGenre.getUpdatedAt().isBefore(updatedGenre.getUpdatedAt())));

        }

        @Test
        public void givenAInvalidName_whenCallsUpdateGenre_ShouldReturnNotificationException() {
                // given
                final var aGenre = Genre.newGenre("Lorem ipsum", true);

                final var expectedId = aGenre.getId();
                final String expectedName = null;
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateGenreCommand.with(
                                expectedId.getValue(),
                                expectedName,
                                expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.findById(any()))
                                .thenReturn(Optional.of(Genre.with(aGenre)));

                // when
                final var actualOutput = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(aCommand));

                // then
                Assertions.assertEquals(expectedErrorCount, actualOutput.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, actualOutput.getErrors().get(0).message());

                verify(genreGateway, times(1))
                                .findById(eq(expectedId));

                verify(genreGateway, times(0)).update(any());

                verify(categoryGateway, times(0)).existsByIds(any());
        }

        @Test
        public void givenAInvalidName_whenCallsUpdateGenreAndSomeCategoriesIsDoesNotExistis_ShouldReturnNotificationException() {
                // given
                final var filmes = CategoryId.from("123");
                final var series = CategoryId.from("456");
                final var documentarios = CategoryId.from("789");

                final var aGenre = Genre.newGenre("Lorem ipsum", true);

                final var expectedId = aGenre.getId();
                final String expectedName = null;
                final var expectedIsActive = true;
                final var expectedCategories = List.of(
                                filmes, series, documentarios);

                final var expectedErrorCount = 2;
                final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
                final var expectedErrorMessageTwo = "'name' should not be null";

                final var aCommand = UpdateGenreCommand.with(
                                expectedId.getValue(),
                                expectedName,
                                expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.findById(any()))
                                .thenReturn(Optional.of(Genre.with(aGenre)));

                when(categoryGateway.existsByIds(any()))
                                .thenReturn(List.of(filmes));

                // when
                final var actualOutput = Assertions.assertThrows(NotificationException.class,
                                () -> useCase.execute(aCommand));

                // then
                Assertions.assertEquals(expectedErrorCount, actualOutput.getErrors().size());
                Assertions.assertEquals(expectedErrorMessageOne, actualOutput.getErrors().get(0).message());
                Assertions.assertEquals(expectedErrorMessageTwo, actualOutput.getErrors().get(1).message());

                verify(genreGateway, times(1))
                                .findById(eq(expectedId));

                verify(genreGateway, times(0)).update(any());

                verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        }

        private List<String> asString(final List<CategoryId> ids) {
                return ids.stream()
                                .map(CategoryId::getValue)
                                .toList();
        }

}
