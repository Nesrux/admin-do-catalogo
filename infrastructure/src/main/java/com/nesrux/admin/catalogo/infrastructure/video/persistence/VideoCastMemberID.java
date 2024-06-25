package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class VideoCastMemberID {
    @Column(name = "video_id")
    private UUID videoId;

    @Column(name = "cast_member_id")
    private UUID memberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(UUID videoId, UUID castMember_id) {
        this.videoId = videoId;
        this.memberId = castMember_id;
    }

    public static VideoCastMemberID from(final UUID videoId, final UUID memberId) {
        return new VideoCastMemberID(videoId, memberId);
    }

    public UUID getVideoId() {
        return videoId;
    }

    public VideoCastMemberID setVideoId(UUID videoId) {
        this.videoId = videoId;
        return this;
    }

    public UUID getCastMemberId() {
        return memberId;
    }

    public VideoCastMemberID setCastMemberId(UUID memberID) {
        this.memberId = memberID;
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
