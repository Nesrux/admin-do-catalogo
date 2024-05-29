package com.nesrux.admin.catalogo.infrastructure.genre;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nesrux.admin.catalogo.MySQLGatewayTest;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {
    @Autowired
    private CategoryMySQLGateway categoryGateway;
    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void givenAvalidGenre_whenCallsCreateGenre_ShouldPersistGenre() {
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectCategories = List.<CategoryId>of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectCategories);

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persitedGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAvalidGenreWithoutCategories_whenCallsCreateGenre_ShouldPersistGenre() {

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectCategories = List.<CategoryId>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, genreRepository.count());

        final var actualGenre = genreGateway.create(aGenre);

        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(aGenre.getId(), actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(aGenre.getId().getValue()).get();

        Assertions.assertEquals(aGenre.getId().getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persitedGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories));

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
        // given
        final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

        final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));

        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());

        // when
        final var actualGenre = genreGateway.update(
                Genre.with(aGenre)
                        .update(expectedName, expectedIsActive, expectedCategories));

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
        Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrepersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.save(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // when
        genreGateway.deleteById(aGenre.getId());

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAInvalidGenre_whenCallsDeleteById_shouldReturnOk() {
        // given
        Assertions.assertEquals(0, genreRepository.count());

        // when
        genreGateway.deleteById(GenreID.from("1234"));

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    protected void givenAprePersistedGenre_whenCallsGetById_shouldReturnGenre() {
        // given
        final var filmes = categoryGateway
                .create(Category.newCategory("Filmes", null, true));

        final var series = categoryGateway
                .create(Category.newCategory("Series", null, true));

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId();

        aGenre.addCategories(expectedCategories);

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // when
        final var actualGenre = genreGateway.findById(aGenre.getId()).get();

        // then
        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertNull(aGenre.getDeletedAt());
    }

    @Test
    protected void givenAInvalidGenreID_whenCallsGetById_shouldReturnGenreEmpty() {
        // given
        final var expectedId = GenreID.from("12345");

        
        // when
        final var actualGenre = genreGateway.findById(expectedId);
        Assertions.assertEquals(0, genreRepository.count());

        // then
        Assertions.assertTrue(actualGenre.isEmpty());
    }

    private List<CategoryId> sorted(final List<CategoryId> expectedCategories) {
        return expectedCategories.stream()
                .sorted(Comparator.comparing(CategoryId::getValue))
                .toList();
    }
}
