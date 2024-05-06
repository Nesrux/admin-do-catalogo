package com.nesrux.admin.catalogo.infrastructure.category;

import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.category.CategorySearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryMySqlGateway(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public void deleteById(CategoryId anId) {

    }

    @Override
    public Optional<Category> findById(CategoryId anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }
}
