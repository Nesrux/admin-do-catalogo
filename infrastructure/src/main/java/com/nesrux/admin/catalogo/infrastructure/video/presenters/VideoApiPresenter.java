package com.nesrux.admin.catalogo.infrastructure.video.presenters;

import com.nesrux.admin.catalogo.application.video.media.upload.UploadMediaOutput;
import com.nesrux.admin.catalogo.application.video.retrive.get.VideoOutput;
import com.nesrux.admin.catalogo.application.video.retrive.list.VideoListOutput;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.video.AudioVideoMedia;
import com.nesrux.admin.catalogo.domain.video.ImageMedia;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.*;

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

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt(),
                output.updatedAt()
        );

    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> aPage) {
        return aPage.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(final UploadMediaOutput output) {
        return new UploadMediaResponse(output.videoId(), output.mediaType());
    }
}
