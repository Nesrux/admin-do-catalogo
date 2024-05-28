package com.nesrux.admin.catalogo.infrastructure.genre;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nesrux.admin.catalogo.MySQLGatewayTest;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
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

}
