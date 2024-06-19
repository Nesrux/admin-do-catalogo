package com.nesrux.admin.catalogo.domain.genre;

import java.util.List;
import java.util.Optional;

import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;

public interface GenreGateway {
    Genre create(Genre genre);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre genre);

    Pagination<Genre> findAll(SearchQuery query);

    List<GenreID> existsByIds(List<GenreID> ids);
}