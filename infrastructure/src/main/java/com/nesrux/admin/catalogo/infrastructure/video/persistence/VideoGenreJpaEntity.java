package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import com.nesrux.admin.catalogo.domain.genre.GenreID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
public class VideoGenreJpaEntity {
    @EmbeddedId
    private VideoGenreID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    private VideoGenreJpaEntity(final VideoGenreID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID genre) {
        return new VideoGenreJpaEntity(
                VideoGenreID.from(video.getId(), genre.getValue()),
                video
        );
    }

    public VideoGenreJpaEntity() {
    }

    public VideoGenreID getId() {
        return id;
    }

    public VideoGenreJpaEntity setId(final VideoGenreID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoGenreJpaEntity setVideo(final VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
