package com.nesrux.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nesrux.admin.catalogo.application.category.retrive.get.CategoryOutput;

import java.time.Instant;

public record CategoryApiOutput(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {

    public static CategoryApiOutput from(final CategoryOutput out) {
        return new CategoryApiOutput(
                out.id().getValue(),
                out.name(),
                out.description(),
                out.isActive(),
                out.createdAt(),
                out.UpdatedAt(),
                out.deletedAt());
    }
}
