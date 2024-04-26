package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import com.nesrux.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    @Test
    @DisplayName("instancia nova categoria")
    public void givenAvalidParams_whenCallNewCategory_theninstatiateACategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        assertNotNull(actualCategory);
        assertNotNull(actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());


    }

    @Test
    @DisplayName("Lança exception ao criar entidade Categoria com nome nulo")
    public void givenAnIvalidName_whenCallNewCategoryAndValidate_ThenShouldReciveError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var actualException = assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

    }


}
