package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberCommand;
import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.api.CastMemberAPI;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand =
                CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }


    @Override
    public Pagination<Object> list(String search, int page, int perPage, String sort, String direction) {
        return null;
    }


    @Override
    public Object getById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateById(String id, Object input) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
