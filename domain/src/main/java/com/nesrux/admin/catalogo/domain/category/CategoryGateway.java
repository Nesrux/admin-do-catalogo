package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    Category update(Category aCategory);

    void deleteById(CategoryId anId);

    Optional<Category> findById(CategoryId anId);

    Pagination<Category> findAll(SearchQuery aQuery);

}
