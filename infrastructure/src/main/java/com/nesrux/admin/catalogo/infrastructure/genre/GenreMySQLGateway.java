package com.nesrux.admin.catalogo.infrastructure.genre;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import com.nesrux.admin.catalogo.infrastructure.utils.SpecificationUtils;

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
        return this.repository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre genre) {
        return save(genre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()),
                        aQuery.sort()));

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.repository.findAll(where(where), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList());

    }

    @Override
    public List<GenreID> existsByIds(Iterable<GenreID> ids) {
        throw new UnsupportedOperationException();
    }

    private Genre save(final Genre aGenre) {
        return this.repository
                .save(GenreJpaEntity.from(aGenre))
                .toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

}
