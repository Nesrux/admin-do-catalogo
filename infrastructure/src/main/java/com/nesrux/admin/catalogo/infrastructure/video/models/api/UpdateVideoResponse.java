package com.nesrux.admin.catalogo.infrastructure.video.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {
}
