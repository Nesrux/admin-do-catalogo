package com.nesrux.admin.catalogo.application.category.create;

import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    public void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAvalidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);


        when(categoryGateway
                .create(any()))
                .thenAnswer(returnsFirstArg());


        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .create(argThat(aCategory -> Objects.equals(expectedName, aCategory.getName()) &&
                        Objects.equals(expectedDescription, aCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aCategory.isActive()) &&
                        Objects.nonNull(aCategory.getCreatedAt()) &&
                        Objects.nonNull(aCategory.getUpdatedAt()) &&
                        Objects.nonNull(aCategory.getId()) &&
                        Objects.isNull(aCategory.getDeletedAt())));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShoulReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand
                .with(expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();


        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErros().size());

        verify(categoryGateway,
                times(0))
                .create(any());

    }

    @Test
    public void givenAvalidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);


        when(categoryGateway
                .create(any()))
                .thenAnswer(returnsFirstArg());


        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .create(argThat(aCategory ->
                        Objects.equals(expectedName, aCategory.getName()) &&
                                Objects.equals(expectedDescription, aCategory.getDescription()) &&
                                Objects.equals(expectedIsActive, aCategory.isActive()) &&
                                Objects.nonNull(aCategory.getCreatedAt()) &&
                                Objects.nonNull(aCategory.getUpdatedAt()) &&
                                Objects.nonNull(aCategory.getId()) &&
                                Objects.nonNull(aCategory.getDeletedAt())));
    }

    @Test
    public void givenAvalidCommand_whenGatewayThrowsRandomExcepetion_shouldReturnAException() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assitida";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();


        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Assertions.assertEquals(expectedErrorCount, notification.getErros().size());


        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        verify(categoryGateway, times(1)).create(argThat(aCategory ->
                Objects.equals(expectedName, aCategory.getName()) &&
                        Objects.equals(expectedDescription, aCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aCategory.isActive()) &&
                        Objects.nonNull(aCategory.getCreatedAt()) &&
                        Objects.nonNull(aCategory.getUpdatedAt()) &&
                        Objects.nonNull(aCategory.getId()) &&
                        Objects.isNull(aCategory.getDeletedAt())));

    }
}
