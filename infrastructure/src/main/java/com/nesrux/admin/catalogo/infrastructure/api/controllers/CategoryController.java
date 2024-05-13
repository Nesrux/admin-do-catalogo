package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import com.nesrux.admin.catalogo.infrastructure.api.CategoryAPI;
import com.nesrux.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {
    @Autowired
    private final CreateCategoryUseCase createUseCase;

    public CategoryController(CreateCategoryUseCase createUseCase) {
        Objects.requireNonNull(this.createUseCase = createUseCase);
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
}
