package com.nesrux.admin.catalogo.application.video.media.get;

import com.nesrux.admin.catalogo.domain.resource.Resource;

public record MediaOutput(
        byte[] content,
        String contentType,
        String name
) {
    public static MediaOutput with(final Resource aResouce) {
        return new MediaOutput(
                aResouce.content(),
                aResouce.contentType(),
                aResouce.name()
        );
    }
}
