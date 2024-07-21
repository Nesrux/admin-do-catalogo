package com.nesrux.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
//TODO : refactor this class
public record AudioVideoMediaResponse(
        @JsonProperty("id") String id,
        @JsonProperty("checksum") String checksum,
        @JsonProperty("name") String name,
        @JsonProperty("location") String rawLocation,
        @JsonProperty("encoded_location") String encodedLocation,
        @JsonProperty("status") String status
) {
}
