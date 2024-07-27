package com.nesrux.admin.catalogo.application.category.update;

import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class UpdateCategoryUsecaseTest extends UseCaseTest {
        @InjectMocks
        private DefaultUpdateCategoryUseCase useCase;

        @Mock
        private CategoryGateway categoryGateway;

        @Override
        protected List<Object> getMocks() {
                return List.of(categoryGateway);
        }

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
                                .update(argThat(aUpdatedCategory -> Objects.equals(expectedName,
                                                aUpdatedCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdatedCategory.getDescription())
                                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdatedCategory.getCreatedAt())
                                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
                                                && aUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt())));
                // .update(argThat(aUpdatedCategory -> Objects.equals(expectedName,
                // aUpdatedCategory.getName()));

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

                when(categoryGateway.findById(eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));

                final var notification = useCase.execute(aCommand).getLeft();

                Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
                Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

                Mockito.verify(categoryGateway, times(0)).update(any());
        }

        @Test
        public void givenAcalidInactivateCommand_whenCallsUpdateCategory_shouldReturInactiveCategoryId() {
                final var aCategory = Category.newCategory("Filne", null, true);

                Assertions.assertTrue(aCategory.isActive());
                Assertions.assertNull(aCategory.getDeletedAt());

                final var expectedName = "filme";
                final var expectedDescription = "A categoria mais acessada";
                final var expectedIsActive = false;

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

                Assertions.assertTrue(aCategory.isActive());
                Assertions.assertNull(aCategory.getDeletedAt());

                final var actualOutput = useCase.execute(aCommand).get();

                Assertions.assertNotNull(actualOutput);
                Assertions.assertNotNull(actualOutput.id());

                verify(categoryGateway, times(1))
                                .findById(expectedId);

                verify(categoryGateway, times(1))
                                .update(argThat(aUpdatedCategory -> Objects.equals(expectedName,
                                                aUpdatedCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdatedCategory.getDescription())
                                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdatedCategory.getCreatedAt())
                                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
                                                && aUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt())));
        }

        @Test
        public void givenAvalidCommand_whenGatewayThrowsRandomExcepetion_shouldReturnAException() {
                final var aCategory = Category.newCategory("Filne", null, true);

                final var expectedName = "filmes";
                final var expectedId = aCategory.getId();
                final var expectedDescription = "A categoria mais assitida";
                final var expectedIsActive = true;
                final var expectedErrorCount = 1;
                final var expectedErrorMessage = "Gateway error";

                final var aCommand = UpdateCategoryCommand.with(
                                expectedId.getValue(),
                                expectedName, expectedDescription,
                                expectedIsActive);

                when(categoryGateway
                                .findById(eq(expectedId)))
                                .thenReturn(Optional.of(aCategory.clone()));

                when(categoryGateway.update(any()))
                                .thenThrow(new IllegalStateException(expectedErrorMessage));

                final var notification = useCase.execute(aCommand).getLeft();

                Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
                Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

                Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
                verify(categoryGateway, times(1))
                                .update(argThat(aUpdatedCategory -> Objects.equals(expectedName,
                                                aUpdatedCategory.getName())
                                                && Objects.equals(expectedDescription,
                                                                aUpdatedCategory.getDescription())
                                                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                                && Objects.equals(aCategory.getCreatedAt(),
                                                                aUpdatedCategory.getCreatedAt())
                                                && Objects.isNull(aUpdatedCategory.getDeletedAt())
                                                && aUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt())));

        }

        @Test
        public void givenCommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundReturn() {
                final var expectedName = "filme";
                final var expectedDescription = "A categoria mais acessada";
                final var expectedIsActive = false;
                final var expectedId = "123";
                final var expectedErrorMessage = "Category with ID 123 was not found";
                final var expectedErrorCount = 1;

                final var aCommand = UpdateCategoryCommand.with(
                                expectedId,
                                expectedName, expectedDescription,
                                expectedIsActive);

                when(categoryGateway
                                .findById(eq(CategoryID.from(expectedId))))
                                .thenReturn(Optional.empty());

                final var actualException = Assertions.assertThrows(DomainException.class,
                                () -> useCase.execute(aCommand));

                Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
                Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

                verify(categoryGateway, times(1))
                                .findById(CategoryID.from(expectedId));

                verify(categoryGateway, times(0)).update(any());
        }
}
