package com.nesrux.admin.catalogo.application.video.media.upload;

import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.video.MediaResourceGateway;
import com.nesrux.admin.catalogo.domain.video.Video;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import com.nesrux.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUploadMediaUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());
        final var aResource = aCommand.resource();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notfound(anId));

        switch (aResource.type()) {
            case VIDEO -> aVideo.setVideo(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.setTrailer(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case BANNER -> aVideo.setBanner(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.setThumbnail(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(mediaResourceGateway.storeImage(anId, aResource));
        }
        return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.type());
    }

    private NotFoundException notfound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
