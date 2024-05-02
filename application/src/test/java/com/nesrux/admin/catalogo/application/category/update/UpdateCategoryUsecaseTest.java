package com.nesrux.admin.catalogo.application.category.update;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUsecaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;


    @Test
    public void givenAvalidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Filne", null, false);
        final var expectedName = "filme";
        final var expectedDescription = "A categoria mais acessada";
        final var expectedIsActive = true;


        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                aCategory.getId().getValue(),
                expectedName, expectedDescription,
                expectedIsActive);

        when(categoryGateway
                .findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
                .findById(expectedId);


        verify(categoryGateway, times(1))
                .update(argThat(aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
                                && aUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt())
                ));
//                .update(argThat(aUpdatedCategory -> Objects.equals(expectedName, aUpdatedCategory.getName()));

    }

    @Test
    public void givenInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainExcepion() {
        final var aCategory = Category.newCategory("film", null, true);
        final var expectedId = aCategory.getId();
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(),
                expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.
                findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.with(aCategory)));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErros().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).update(any());
    }
}


