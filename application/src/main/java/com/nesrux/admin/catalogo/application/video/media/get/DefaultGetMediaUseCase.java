package com.nesrux.admin.catalogo.application.video.media.get;

import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.video.MediaResourceGateway;
import com.nesrux.admin.catalogo.domain.video.VideoID;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {
    private final MediaResourceGateway resourceGateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway resourceGateway) {
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
    }


    @Override
    public MediaOutput execute(final GetMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aType = VideoMediaType.of(aCommand.mediaType())
                .orElseThrow(() -> typeNotFound(aCommand.mediaType()));

        final var aResouce =
                this.resourceGateway.getResource(anId, aType)
                        .orElseThrow(() -> notFound(anId, aType));

        return MediaOutput.with(aResouce);
    }

    private NotFoundException typeNotFound(final String type) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(type)));

    }

    private NotFoundException notFound(final VideoID anId, final VideoMediaType aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType.name(), anId.getValue())));

    }
}
