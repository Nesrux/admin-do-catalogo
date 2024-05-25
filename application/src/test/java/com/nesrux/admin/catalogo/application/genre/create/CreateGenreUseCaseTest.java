package com.nesrux.admin.catalogo.application.genre.create;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {
        @InjectMocks
        private DefaultCreateGenreUseCase useCase;
        @Mock
        private GenreGateway genreGateway;
        @Mock
        private CategoryGateway categoryGateway;

        @Test
        public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
                // given
                final var expectedName = "Ação";
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                verify(genreGateway, times(1))
                                .create(argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                                                && Objects.equals(expectedIsActive, aGenre.isActive())
                                                && Objects.equals(expectedCategories, aGenre.getCategories())
                                                && Objects.nonNull(aGenre.getId())
                                                && Objects.nonNull(aGenre.getCreatedAt())
                                                && Objects.nonNull(aGenre.getUpdatedAt())
                                                && Objects.isNull(aGenre.getDeletedAt())));
        }

        @Test
        public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
                // given
                final var expectedName = "Ação";
                final var expectedIsActive = false;
                final var expectedCategories = List.<CategoryId>of();

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));

                when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                verify(genreGateway, times(1))
                                .create(argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                                                && Objects.equals(expectedIsActive, aGenre.isActive())
                                                && Objects.equals(expectedCategories, aGenre.getCategories())
                                                && Objects.nonNull(aGenre.getId())
                                                && Objects.nonNull(aGenre.getCreatedAt())
                                                && Objects.nonNull(aGenre.getUpdatedAt())
                                                && Objects.nonNull(aGenre.getDeletedAt())));
        }


        @Test
        public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldREturnGenreIds() {
                // given
                final var expectedName = "Ação";
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of(
                                CategoryId.from("1234"),
                                CategoryId.from("456"),
                                CategoryId.from("789"));

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));

                when(categoryGateway.existsByIds(any()))
                                .thenReturn(expectedCategories);

                when(genreGateway.create(any()))
                                .thenAnswer(returnsFirstArg());

                // when
                final var actualOutput = useCase.execute(aCommand);

                // then
                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                verify(categoryGateway, times(1)).existsByIds(expectedCategories);

                verify(genreGateway, times(1))
                                .create(argThat(aGenre -> Objects.equals(expectedName, aGenre.getName())
                                                && Objects.equals(expectedIsActive, aGenre.isActive())
                                                && Objects.equals(expectedCategories, aGenre.getCategories())
                                                && Objects.nonNull(aGenre.getId())
                                                && Objects.nonNull(aGenre.getCreatedAt())
                                                && Objects.nonNull(aGenre.getUpdatedAt())
                                                && Objects.isNull(aGenre.getDeletedAt())));

        }

        @Test
        public void givenInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
                // given
                final var expectedName = "  ";
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var expectedErrorMessage = "'name' should not be empty";
                final var expectedErrorCount = 1;

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));
                // when
                final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
                        useCase.execute(aCommand);
                });

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

                verify(categoryGateway, times(0)).existsByIds(any());
                verify(genreGateway, times(0)).create(any());

        }

        @Test
        public void givenInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
                // given
                final String expectedName = null;
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));
                // when
                final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
                        useCase.execute(aCommand);
                });

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

                verify(categoryGateway, times(0)).existsByIds(any());
                verify(genreGateway, times(0)).create(any());

        }

        @Test
        public void givenInvalidNsullName_whenCallsCreateGenre_shouldReturnDomainException() {
                // given
                final String expectedName = null;
                final var expectedIsActive = true;
                final var expectedCategories = List.<CategoryId>of();

                final var expectedErrorMessage = "'name' should not be null";
                final var expectedErrorCount = 1;

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));
                // when
                final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
                        useCase.execute(aCommand);
                });

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

                verify(categoryGateway, times(0)).existsByIds(any());
                verify(genreGateway, times(0)).create(any());

        }

        @Test
        public void givenAvalidCommand_whenCallsCreateAGenreAndSomeCategoriesDoesNotExistis_shouldReturnDomainException() {
                // given
                final var expectedName = "Aventura";
                final var expectedIsActive = true;
                final var filmes = CategoryId.from("123");
                final var series = CategoryId.from("456");
                final var documentarios = CategoryId.from("789");
                final var expectedCategories = List.<CategoryId>of(
                                filmes, series, documentarios);

                final var expectedErrorMessage = "Some categories could not be found: 123, 789";
                final var expectedErrorCount = 1;

                when(categoryGateway.existsByIds(any()))
                                .thenReturn(List.of(series));

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));
                // when
                final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
                        useCase.execute(aCommand);
                });

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

                verify(categoryGateway, times(1)).existsByIds(any());
                verify(genreGateway, times(0)).create(any());

        }

        @Test
        public void givenAInvalidName_whenCallsCreateAGenreAndSomeCategoriesDoesNotExistis_shouldReturnDomainException() {
                // given
                final var expectedName = " ";
                final var expectedIsActive = true;
                final var filmes = CategoryId.from("123");
                final var series = CategoryId.from("456");
                final var documentarios = CategoryId.from("789");
                final var expectedCategories = List.<CategoryId>of(
                                filmes, series, documentarios);

                final var expectedErrorMessageOne = "Some categories could not be found: 123, 789";
                final var expectedErrorMessagesTwo = "'name' should not be empty";

                final var expectedErrorCount = 2;

                when(categoryGateway.existsByIds(any()))
                                .thenReturn(List.of(series));

                final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive,
                                asString(expectedCategories));
                // when
                final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
                        useCase.execute(aCommand);
                });

                // then
                Assertions.assertNotNull(actualException);
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
                Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
                Assertions.assertEquals(expectedErrorMessagesTwo, actualException.getErrors().get(1).message());

                verify(categoryGateway, times(1)).existsByIds(any());
                verify(genreGateway, times(0)).create(any());

        }

      

        private List<String> asString(final List<CategoryId> categories) {
                return categories.stream()
                                .map(CategoryId::getValue)
                                .toList();
        }
}
