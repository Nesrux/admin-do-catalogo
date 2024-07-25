package com.nesrux.admin.catalogo.infrastructure.configuration.usecase;

import com.nesrux.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.create.DefaultCreateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.delete.DefaultDeleteVideoUseCase;
import com.nesrux.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.nesrux.admin.catalogo.application.video.media.get.DefaultGetMediaUseCase;
import com.nesrux.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.nesrux.admin.catalogo.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.nesrux.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.nesrux.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.nesrux.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.DefaultGetVideoByIdUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.GetVideoByIdUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.list.DefaultListVideoUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.list.ListVideosUseCase;
import com.nesrux.admin.catalogo.application.video.update.DefaultUpdateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.video.MediaResourceGateway;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {
    private final VideoGateway videoGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final CategoryGateway categoryGateway;

    public VideoUseCaseConfig(
            final VideoGateway videoGateway,
            final CastMemberGateway castMemberGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(
                categoryGateway,
                genreGateway,
                castMemberGateway,
                videoGateway,
                mediaResourceGateway
        );
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(
                videoGateway,
                mediaResourceGateway,
                genreGateway,
                castMemberGateway,
                categoryGateway
        );
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideoUseCase(videoGateway);
    }

}
