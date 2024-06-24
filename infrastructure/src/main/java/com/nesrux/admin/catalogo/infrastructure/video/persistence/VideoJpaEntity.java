package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import com.nesrux.admin.catalogo.domain.video.Rating;
import com.nesrux.admin.catalogo.domain.video.Video;
import com.nesrux.admin.catalogo.domain.video.VideoID;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.UUID;

@Entity(name = "Video")
@Table(name = "videos")
public class VideoJpaEntity {
    @Id
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "year_launched")
    private int yerarLaunched;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "rating", nullable = false)
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public VideoJpaEntity() {
    }

    public VideoJpaEntity(
            final UUID id,
            final String title,
            final String description,
            final int yerarLaunched,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final double duration,
            final Instant createdAt,
            final Instant updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yerarLaunched = yerarLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static VideoJpaEntity from(final Video aVideo) {
        return new VideoJpaEntity(
                UUID.fromString(aVideo.getId().getValue()),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt()
        );
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYerarLaunched()),
                getDuration(),
                getOpened(),
                getPublished(),
                getRating(),
                getCreatedAt(),
                getUpdatedAt(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null


        );

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYerarLaunched() {
        return yerarLaunched;
    }

    public void setYerarLaunched(int yerarLaunched) {
        this.yerarLaunched = yerarLaunched;
    }

    public boolean getOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean getPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
