package com.nesrux.admin.catalogo.infrastructure.castmember.presenter;

import com.nesrux.admin.catalogo.application.castmember.retrive.get.CastMemberOutput;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt(),
                aMember.updatedAt());
    }
}
