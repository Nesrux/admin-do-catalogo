package com.nesrux.admin.catalogo.domain.genre;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;

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

    @Test
    public void givenAnActiveGenre_WhenCallDeactivate_ShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();
        actualGenre.deactivate();

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));

        Assertions.assertNotNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidInactiveGenre_WhenCallUpdateWithInactivate_ShouldReceiveUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("1234"));

        final var actualGenre = Genre.newGenre("assão", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_WhenCallUpdate_ShouldReceiveUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryId.from("1234"));

        final var actualGenre = Genre.newGenre("assão", true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_WhenCallUpdatewithEmptyName_ShouldReceiveNotificationException() {
        final var expectedName = "   ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("1234"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre("assão", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidInactiveGenre_WhenCallUpdatewithNullName_ShouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("1234"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("assão", false);

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_WhenCallUpdateWithNullCategories_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryId>();

        final var actualGenre = Genre.newGenre("assão", expectedIsActive);
        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        Assertions.assertDoesNotThrow(() -> actualGenre.update(expectedName, expectedIsActive, null));

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_WhenCallAddCategory_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryId.from("123");
        final var filmesId = CategoryId.from("456");
        final var expectedCategories = List.of(seriesId, filmesId);

        final var actualGenre = Genre.newGenre("assão", expectedIsActive);
        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesId);
        actualGenre.addCategory(filmesId);

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_WhenCallremove_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryId.from("123");
        final var filmesId = CategoryId.from("456");
        final var expectedCategories = List.of(filmesId);

        final var actualGenre = Genre.newGenre("assão", expectedIsActive);
        Assertions.assertEquals(0, actualGenre.getCategories().size());

        actualGenre.update(expectedName, expectedIsActive, List.of(seriesId, filmesId));
        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesId);
        Assertions.assertEquals(1, actualGenre.getCategories().size());

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_WhenCallremove_ShouldReceiveOk() {
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryId.from("123");
        final var filmesId = CategoryId.from("456");
        final var expectedCategories = List.of(seriesId, filmesId);

        final var actualGenre = Genre.newGenre("assão", expectedIsActive);
        Assertions.assertEquals(0, actualGenre.getCategories().size());

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        Assertions.assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);
        Assertions.assertEquals(1, actualGenre.getCategories().size());

        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

}
