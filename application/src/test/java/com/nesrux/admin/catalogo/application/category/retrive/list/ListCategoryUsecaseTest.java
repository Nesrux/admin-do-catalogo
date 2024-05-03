package com.nesrux.admin.catalogo.application.category.retrive.list;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategorySearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ListCategoryUsecaseTest {
    @InjectMocks
    private DefaultListCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        reset(categoryGateway);
    }

    @Test
    public void GivenAvalidQuery_WhenCallsListCategorys_ThenShouldReturnCategories() {
        final List<Category> categories = List.of(
                Category.newCategory("Filmes", "só o filé", true),
                Category.newCategory("Animes", "Só os melhores da temporada", true)
        );
        final var expectedPage = 0;
        final var expectedperPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedPagination = new Pagination<>(expectedPage, expectedperPage, categories.size(), categories);


        final var aQuery = new CategorySearchQuery(expectedPage, expectedperPage, expectedTerms, expectedSort, expectedDirection);


        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.itens().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedperPage, actualResult.perPage());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void GivenAValidQuery_whenHasNoResults_thenShouldREturnEmptyCategories() {
    }

    @Test
    public void givenAvalidQuery_whenGatewayThrowsException_shouldReturnException() {
    }
}
