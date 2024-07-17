package com.nesrux.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoMetadata(
        @JsonProperty("encoded_video_folder") String encondedVideoFolder,
        @JsonProperty("resource_id") String resourceId,
        @JsonProperty("file_path") String filePath
) {
}
