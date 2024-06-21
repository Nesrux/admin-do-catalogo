package com.nesrux.admin.catalogo.application.video.retrive.get;

import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.video.Video;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import com.nesrux.admin.catalogo.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetByIdUseCase {
    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }


    @Override
    public VideoOutput execute(String anId) {
        final var aVideoID = VideoID.from(anId);
        return this.videoGateway.findById(aVideoID)
                .map(VideoOutput::from)
                .orElseThrow(() ->
                        NotFoundException.with(Video.class, aVideoID));
    }
}
