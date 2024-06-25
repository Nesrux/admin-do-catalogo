package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCastMemberID implements Serializable {
    @Column(name = "video_id")
    private UUID videoId;

    @Column(name = "cast_member_id")
    private UUID castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(UUID videoId, UUID castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberID from(final UUID videoId, final UUID castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }



    public UUID getVideoId() {
        return videoId;
    }

    public VideoCastMemberID setVideoId(UUID videoId) {
        this.videoId = videoId;
        return this;
    }

    public UUID getCastMemberId() {
        return castMemberId;
    }

    public VideoCastMemberID setCastMemberId(UUID castMemberId) {
        this.castMemberId = castMemberId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberID that = (VideoCastMemberID) o;
        return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(getCastMemberId(), that.getCastMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getCastMemberId());
    }
}
