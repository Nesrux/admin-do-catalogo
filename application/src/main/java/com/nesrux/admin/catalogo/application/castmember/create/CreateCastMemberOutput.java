package com.nesrux.admin.catalogo.application.castmember.create;

import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(
        String id
) {
    public static CreateCastMemberOutput from(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }
    public static CreateCastMemberOutput from(final CastMemberID castMemberID) {
        return new CreateCastMemberOutput(castMemberID.getValue());
    }
}
