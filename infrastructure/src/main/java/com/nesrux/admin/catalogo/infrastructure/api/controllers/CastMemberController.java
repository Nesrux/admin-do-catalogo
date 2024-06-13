package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.GetCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberCommand;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.nesrux.admin.catalogo.infrastructure.castmember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberUseCase getCastMemberUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberUseCase getCastMemberUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase) {

        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberUseCase = Objects.requireNonNull(getCastMemberUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
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
        return null;
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
    public void deleteById(final String id) {

    }
}
