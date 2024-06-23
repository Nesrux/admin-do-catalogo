package com.nesrux.admin.catalogo.application.video.update;

import com.nesrux.admin.catalogo.domain.video.Video;

public record UpdateVideoOutput(String id) {
    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}
