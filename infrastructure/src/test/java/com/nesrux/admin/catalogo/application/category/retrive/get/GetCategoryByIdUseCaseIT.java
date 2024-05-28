package com.nesrux.admin.catalogo.application.category.retrive.get;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {
        @Autowired
        private GetCategoryByIdUseCase useCase;

        @Autowired
        private CategoryRepository categoryRepository;

        @SpyBean
        private CategoryGateway categoryGateway;

        @Test
        public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
                final var expectedName = "Filmes";
                final var expectedDescription = "A categoria mais assistida";
                final var expectedIsActive = true;
                final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
                final var expectedId = aCategory.getId();

                save(aCategory);

                final var actualCategory = useCase.execute(expectedId.getValue());

                Assertions.assertEquals(expectedId, actualCategory.id());
                Assertions.assertEquals(expectedName, actualCategory.name());
                Assertions.assertEquals(expectedDescription, actualCategory.description());
                Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
                Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());

                /*
                 * Por causa do problema com o H2 necessario truncar o tempo, pois ele não
                 * consegue chegar em nano segundso
                 */
                Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
                Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.UpdatedAt());
        }

        @Test
        public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
                final var expectedErrorMessage = "Category with ID 123 was not found";
                final var expectedId = CategoryId.from("123");

                final var actualException = Assertions.assertThrows(
                                DomainException.class,
                                () -> useCase.execute(expectedId.getValue()));

                Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        }

        @Test
        public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
                final var expectedErrorMessage = "Gateway error";
                final var expectedId = CategoryId.from("123");

                doThrow(new IllegalStateException(expectedErrorMessage))
                                .when(categoryGateway).findById(eq(expectedId));

                final var actualException = Assertions.assertThrows(
                                IllegalStateException.class,
                                () -> useCase.execute(expectedId.getValue()));

                Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        }

        private void save(final Category... aCategory) {
                categoryRepository.saveAllAndFlush(
                                Arrays.stream(aCategory)
                                                .map(CategoryJpaEntity::from)
                                                .toList());
        }
}
