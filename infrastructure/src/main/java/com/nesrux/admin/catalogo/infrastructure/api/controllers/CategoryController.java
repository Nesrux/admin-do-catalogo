package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import com.nesrux.admin.catalogo.infrastructure.api.CategoryAPI;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.nesrux.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.nesrux.admin.catalogo.infrastructure.category.models.UpdateCategoryApiOutput;
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

    public CategoryController(final CreateCategoryUseCase createUseCase,
                              final GetCategoryByIdUseCase getCategoryByIdUseCase,
                              final UpdateCategoryUseCase updateCategoryUseCase,
                              final DeleteCategoryUseCase deleteCategoryUseCase) {

        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.createUseCase = Objects.requireNonNull(createUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        //Eu poderia devolver tudo isso em uma mesma função, mas ficaria menos legivel :(
        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> ResponseEntity
                .created(URI.create("/categories/" + output.id())).body(output);

        return this.createUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String dir) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(String id) {
        return CategoryApiOutput.from(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiOutput input) {
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
