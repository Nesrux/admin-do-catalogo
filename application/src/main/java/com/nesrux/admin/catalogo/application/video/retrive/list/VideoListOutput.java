package com.nesrux.admin.catalogo.application.video.retrive.list;

import com.nesrux.admin.catalogo.domain.video.Video;

import java.time.Instant;

public record VideoListOutput(
        String id,
        String title,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static VideoListOutput from(final Video avideo) {
        return new VideoListOutput(
                avideo.getId().getValue(),
                avideo.getTitle(),
                avideo.getDescription(),
                avideo.getCreatedAt(),
                avideo.getUpdatedAt()

        );
    }
}
