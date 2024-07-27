package com.nesrux.admin.catalogo.infrastructure.category;

import static com.nesrux.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.nesrux.admin.catalogo.infrastructure.utils.SpecificationUtils;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository categoryRepository) {
        this.repository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        final var idValue = anId.getValue();

        if (repository.existsById(idValue)) {
            repository.deleteById(idValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        // Paginação
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort()));

        final var where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> SpecificationUtils.<CategoryJpaEntity>like("name", str)
                        .or(like("description", str)))
                .orElse(null);

        final var pageResult = this.repository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate)
                        .toList());
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIds) {
        final var ids = StreamSupport.stream(categoryIds.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();
        return this.repository.existsByIds(ids)
                .stream()
                .map(CategoryID::from)
                .toList();
    }
}
