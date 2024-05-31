package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.list.ListCategoriesUseCase;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import com.nesrux.admin.catalogo.infrastructure.api.CategoryAPI;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.nesrux.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.nesrux.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.nesrux.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(final CreateCategoryUseCase createUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase) {

        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.createUseCase = Objects.requireNonNull(createUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        // Eu poderia devolver tudo isso em uma mesma função, mas ficaria menos legivel
        // :(
        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity
                .created(URI.create("/categories/" + output.id())).body(output);

        return this.createUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String dir) {

        return listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        return CategoryResponse.from(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active());

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
