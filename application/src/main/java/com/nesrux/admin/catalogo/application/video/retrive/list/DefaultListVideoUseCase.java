package com.nesrux.admin.catalogo.application.video.retrive.list;

import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import com.nesrux.admin.catalogo.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideoUseCase extends ListVideosUseCase {
    private final VideoGateway videoGateway;

    public DefaultListVideoUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery)
                .map(VideoListOutput::from);
    }
}
