package com.nesrux.admin.catalogo.application.category.retrive.list;

import com.nesrux.admin.catalogo.application.UseCase;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
