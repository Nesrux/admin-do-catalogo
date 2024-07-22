package com.nesrux.admin.catalogo.infrastructure.video.presenters;

import com.nesrux.admin.catalogo.application.video.retrive.get.VideoOutput;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.nesrux.admin.catalogo.domain.video.AudioVideoMedia;
import com.nesrux.admin.catalogo.domain.video.ImageMedia;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.AudioVideoMediaResponse;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.ImageMediaResponse;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.UpdateVideoResponse;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoResponse;

public interface VideoApiPresenter {
    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.video()),
                present(output.trailer()),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static ImageMediaResponse present(final ImageMedia media) {
        if (media == null) return null;
        return new ImageMediaResponse(media.id(), media.checksum(), media.name(), media.location());
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) return null;
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }
}
