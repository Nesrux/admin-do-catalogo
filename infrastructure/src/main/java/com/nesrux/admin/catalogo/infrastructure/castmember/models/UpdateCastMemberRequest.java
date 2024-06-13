package com.nesrux.admin.catalogo.infrastructure.castmember.models;

import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name , CastMemberType type) {
}
