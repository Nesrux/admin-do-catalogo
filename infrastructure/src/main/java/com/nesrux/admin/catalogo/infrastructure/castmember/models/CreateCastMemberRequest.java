package com.nesrux.admin.catalogo.infrastructure.castmember.models;

import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
        String name, CastMemberType type) {
}
