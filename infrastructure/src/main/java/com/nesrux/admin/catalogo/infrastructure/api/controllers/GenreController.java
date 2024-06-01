package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.nesrux.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.api.GenreAPI;
import com.nesrux.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.nesrux.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.nesrux.admin.catalogo.infrastructure.genre.models.GenreResponse;
import com.nesrux.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import com.nesrux.admin.catalogo.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase, GetGenreByIdUseCase getGenreByIdUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories());

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(String search, int page, int perPage, String sort, String dir) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public GenreResponse getById(String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateGenreRequest body) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateById'");
    }

    @Override
    public void deleteById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

}
