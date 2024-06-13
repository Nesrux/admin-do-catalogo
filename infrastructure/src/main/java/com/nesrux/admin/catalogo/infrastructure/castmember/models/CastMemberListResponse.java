package com.nesrux.admin.catalogo.infrastructure.castmember.models;

import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListResponse(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {
}
