package com.nesrux.admin.catalogo.application.video.retrive.get;

import com.nesrux.admin.catalogo.domain.Identifier;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.utils.CollectionsUtil;
import com.nesrux.admin.catalogo.domain.video.AudioVideoMedia;
import com.nesrux.admin.catalogo.domain.video.ImageMedia;
import com.nesrux.admin.catalogo.domain.video.Rating;
import com.nesrux.admin.catalogo.domain.video.Video;

import java.util.Set;
import java.util.stream.Collectors;

public record VideoOutput(
        String id,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf,
        AudioVideoMedia video,
        AudioVideoMedia treiler
) {
    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                CollectionsUtil.mapTo(aVideo.getCategories(), CategoryID::getValue),
                CollectionsUtil.mapTo(aVideo.getGenres(), GenreID::getValue),
                CollectionsUtil.mapTo(aVideo.getCastMembers(), CastMemberID::getValue),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null)
        );

    }
}
