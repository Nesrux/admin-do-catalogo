package com.nesrux.admin.catalogo.application.castmember.retrive.get;

import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberUseCase {
    private final CastMemberGateway gateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CastMemberOutput execute(final String anId) {
        final var aMemberID = CastMemberID.from(anId);
        return this.gateway.findById(aMemberID)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberID));
    }
}
