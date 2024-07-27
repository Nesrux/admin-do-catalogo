package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.genre.create.CreateGenreCommand;
import com.nesrux.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.retrive.get.GetGenreByIdUseCase;
import com.nesrux.admin.catalogo.application.genre.retrive.list.ListGenreUseCase;
import com.nesrux.admin.catalogo.application.genre.update.UpdateGenreCommand;
import com.nesrux.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
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
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
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
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateGenreRequest body) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                body.name(),
                body.isActive(),
                body.categories());

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);

    }

    @Override
    public void deleteById(String id) {
        this.deleteGenreUseCase.execute(id);
    }

}
