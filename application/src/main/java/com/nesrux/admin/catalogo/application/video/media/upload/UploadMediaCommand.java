package com.nesrux.admin.catalogo.application.video.media.upload;

import com.nesrux.admin.catalogo.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {

    public static UploadMediaCommand with(final String aVideoId, final VideoResource aResource) {
        return new UploadMediaCommand(aVideoId, aResource);
    }
}
