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

    private List<String> asString(final List<CategoryId> ids) {
        return ids.stream()
                .map(CategoryId::getValue)
                .toList();
    }

}
