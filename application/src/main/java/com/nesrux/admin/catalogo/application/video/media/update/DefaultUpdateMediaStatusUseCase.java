package com.nesrux.admin.catalogo.application.video.media.update;

import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.video.*;

import java.util.Objects;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {
    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResourceId = aCommand.resourceId();
        final var folder = aCommand.folder();
        final var filename = aCommand.fileName();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notfound(anId));

        final var encondedPath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, aCommand.status(), aVideo, encondedPath);
        }
        else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, aCommand.status(), aVideo, encondedPath);
        }
    }

    private void updateVideo(final VideoMediaType aType, final MediaStatus status, final Video aVideo, final String encondedPath) {
        switch (status) {
            case PENDING -> {
            }
            case PROCESSING -> aVideo.processing(VideoMediaType.VIDEO);
            case COMPLETED -> aVideo.completed(VideoMediaType.VIDEO, encondedPath);
        }
        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, final AudioVideoMedia aMedia) {
        if (aMedia == null) return false;
        return aMedia.id().equals(anId);
    }

    private NotFoundException notfound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
