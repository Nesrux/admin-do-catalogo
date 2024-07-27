package com.nesrux.admin.catalogo.infrastructure.video.models.rabbitmq;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ERROR")
public record VideoEncoderError(
        @JsonProperty("message") VideoMessage message,
        @JsonProperty("error") String  error
) implements VideoEnconderResult {

    public static final String ERROR = "ERROR";

    @Override
    public String getStatus() {
        return ERROR;
    }
}
