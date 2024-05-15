package com.nesrux.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nesrux.admin.catalogo.application.category.retrive.get.CategoryOutput;

import java.time.Instant;

public record CategoryResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {

    public static CategoryResponse from(final CategoryOutput out) {
        return new CategoryResponse(
                out.id().getValue(),
                out.name(),
                out.description(),
                out.isActive(),
                out.createdAt(),
                out.UpdatedAt(),
                out.deletedAt());
    }
}
