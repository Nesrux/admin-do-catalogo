package com.nesrux.admin.catalogo.domain.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

public class GenreTest {
    @Test
    public void givenAvalidParams_whenCallNewGenre_ShouldIntantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var expectedGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertNotNull(expectedGenre);
        Assertions.assertNotNull(expectedGenre.getId());
        Assertions.assertEquals(expectedName, expectedGenre.getName());
        Assertions.assertEquals(expectedIsActive, expectedGenre.isActive());
        Assertions.assertEquals(expectedCategories, expectedGenre.getCategories().size());
        Assertions.assertNotNull(expectedGenre.getCreatedAt());
        Assertions.assertNotNull(expectedGenre.getUpdatedAt());
        Assertions.assertNull(expectedGenre.getDeletedAt());
    }

    @Test
    public void givenIvalidNullName_whenCallNewGenreAndValidate_ShouldReceiveAError() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenIvalidEmptyName_whenCallNewGenreAndValidate_ShouldReceiveAError() {
        final String expectedName = "  ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenIvalidNameWithLenghtGreaterThan255_whenCallNewGenreAndValidate_ShouldReceiveAError() {
        final String expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et +
                dolore magna aliqua. Ac turpis egestas maecenas pharetra convallis posuere morbi. Adipiscing elit duis +
                tristique sollicitudin nibh sit amet commodo nulla. Leo
                """;

        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });
        
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
