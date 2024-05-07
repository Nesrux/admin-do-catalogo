package com.nesrux.admin.catalogo.infrastructure.category;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.infrastructure.MySqlGatewayTest;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySqlGatewayTest
public class CategoryMySqlGatewayTest {
    @Autowired
    private CategoryMySQLGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAvalidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Só os filé";
        final var isActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, isActive);

        Assertions.assertEquals(0, repository.count());

        final var actualCategory = gateway.create(aCategory);

        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(isActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(isActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAvalidCategory_whenCallsUpdate_shouldReturnACategoryUpdated() {
        final var expectedName = "Filmes";
        final var expectedDescription = "Só os filé";
        final var isActive = true;

        final var aCategory = Category.newCategory("filn", null, isActive);

        Assertions.assertEquals(0, repository.count());

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, repository.count());

        final var actualInvalidEntity = repository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals(aCategory.getId().getValue(), actualInvalidEntity.getId());
        Assertions.assertEquals(aCategory.getName(), actualInvalidEntity.getName());
        Assertions.assertNull(actualInvalidEntity.getDescription());

        final var anUpdatedCategory = aCategory.clone()
                .update(expectedName, expectedDescription, isActive);

        final var actualCategory = gateway.update(anUpdatedCategory);

        Assertions.assertEquals(1, repository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(isActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var actualEntity = repository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(isActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(aCategory.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

}
