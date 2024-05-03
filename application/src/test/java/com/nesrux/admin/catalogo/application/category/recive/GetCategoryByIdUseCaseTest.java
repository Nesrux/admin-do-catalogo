package com.nesrux.admin.catalogo.application.category.recive;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {
    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp() {
        reset(gateway);
    }

    /*
     * 1) teste onde eu passo o id que existe
     * 2) teste de onde o Id não existe
     * 3) teste erro no gatway*/

    @Test
    public void givenAvalidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Só o filé";
        final var isActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);
        final var expectedId = aCategory.getId();

        when(gateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedId, actualCategory.getId());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(isActive, aCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedId = CategoryId.from("123");
        final var expectedErrorMessage = "";

        when(gateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryId.from("123");
        final var expectedErrorMessage = "Gateway Error";

        when(gateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
