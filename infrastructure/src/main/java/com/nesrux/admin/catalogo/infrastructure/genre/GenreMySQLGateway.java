package com.nesrux.admin.catalogo.infrastructure.genre;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreRepository;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository repository;

    public GenreMySQLGateway(GenreRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var aGenreId = anId.getValue();
        if (repository.existsById(aGenreId)) {
            repository.deleteById(aGenreId);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Genre update(final Genre genre) {
        return save(genre);
    }

    @Override
    public Pagination<Genre> findAll(SearchQuery query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    private Genre save(final Genre aGenre) {
        return this.repository
                .save(GenreJpaEntity.from(aGenre))
                .toAggregate();
    }

}
