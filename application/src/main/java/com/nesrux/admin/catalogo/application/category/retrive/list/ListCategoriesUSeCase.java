package com.nesrux.admin.catalogo.application.category.retrive.list;

import com.nesrux.admin.catalogo.application.UseCase;
import com.nesrux.admin.catalogo.domain.category.CategorySearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUSeCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
