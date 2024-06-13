package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.GetCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.list.ListCastMembersUseCase;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.nesrux.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberUseCase getCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberUseCase getCastMemberUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase, ListCastMembersUseCase listCastMembersUseCase) {

        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberUseCase = Objects.requireNonNull(getCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand =
                CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }


    @Override
    public Pagination<Object> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }


    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest input) {
        final var aCommand = UpdateCastMemberCommand
                .with(id, input.name(), input.type());

        final var output = this.updateCastMemberUseCase.execute(aCommand);
        return ResponseEntity.ok(output);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(final String id) {
        deleteCastMemberUseCase.execute(id);
    }
}
