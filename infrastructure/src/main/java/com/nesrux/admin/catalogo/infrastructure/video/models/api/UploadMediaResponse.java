package com.nesrux.admin.catalogo.infrastructure.video.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}
