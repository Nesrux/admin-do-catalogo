package com.nesrux.admin.catalogo.application.category.create;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
public class CreateCategoryUseCaseTestIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAvalidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory =
                categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShoulReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand
                .with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();


        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(0, categoryRepository.count());

        Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAvalidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = false;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory
                = categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAvalidCommand_whenGatewayThrowsRandomExcepetion_shouldReturnAException() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                        .when(categoryGateway).create(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    }

}

