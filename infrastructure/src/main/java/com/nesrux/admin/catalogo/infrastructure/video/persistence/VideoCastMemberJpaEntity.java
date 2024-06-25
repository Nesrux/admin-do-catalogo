package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJpaEntity {
    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    private VideoCastMemberJpaEntity(final VideoCastMemberID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }


    public VideoCastMemberJpaEntity() {
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity video, final CastMemberID member) {
        return new VideoCastMemberJpaEntity(
                VideoCastMemberID.from(video.getId(), member.getValue()),
                video
        );
    }

    public VideoCastMemberID getId() {
        return id;
    }

    public VideoCastMemberJpaEntity setId(VideoCastMemberID id) {
        this.id = id;
        return this;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public VideoCastMemberJpaEntity setVideo(VideoJpaEntity video) {
        this.video = video;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getVideo(), that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getVideo());
    }
}
