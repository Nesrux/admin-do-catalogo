package com.nesrux.admin.catalogo.application.genre.retrive.list;

import com.nesrux.admin.catalogo.application.UseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

}
