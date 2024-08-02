package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.video.create.CreateVideoCommand;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.nesrux.admin.catalogo.application.video.media.get.GetMediaCommand;
import com.nesrux.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.nesrux.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.nesrux.admin.catalogo.application.video.media.upload.UploadMediaUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.GetVideoByIdUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.list.ListVideosUseCase;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;
import com.nesrux.admin.catalogo.domain.video.VideoResource;
import com.nesrux.admin.catalogo.domain.video.VideoSearchQuery;
import com.nesrux.admin.catalogo.infrastructure.api.VideoApi;
import com.nesrux.admin.catalogo.infrastructure.utils.HashingUtils;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.CreateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.UpdateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoListResponse;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoResponse;
import com.nesrux.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.nesrux.admin.catalogo.domain.utils.CollectionsUtil.mapTo;

@RestController
public class VideoController implements VideoApi {
    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase) {

        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final String aRating,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCommd = CreateVideoCommand.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                members,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile));

        final var output = this.createVideoUseCase.execute(aCommd);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCommd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.duration(),
                payload.yearLaunched(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(aCommd);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String id) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateVideoRequest payload) {
        final var aCommand = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );
        final var output = this.updateVideoUseCase.execute(aCommand);

        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres) {

        final var membersId = mapTo(castMembers, CastMemberID::from);
        final var categoriesId = mapTo(categories, CategoryID::from);
        final var genreIds = mapTo(genres, GenreID::from);

        final var aQuery = new VideoSearchQuery(page, perPage, search, sort, direction, membersId, categoriesId, genreIds);
        final var aPage = this.listVideosUseCase.execute(aQuery);
        return VideoApiPresenter.present(aPage);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String id, final String type) {
        final var aMedia =
                this.getMediaUseCase.execute(GetMediaCommand.with(id, type));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var aType = VideoMediaType.of(type)
                .orElseThrow(() -> new IllegalArgumentException("Invalid %s for VideoMediaType".formatted(type)));

        final var aCommand = UploadMediaCommand
                .with(id, VideoResource.with(resourceOf(media), aType));
        final var output = this.uploadMediaUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/videos/%s/medias/%s".formatted(id, aType.name())))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile file) {
        if (file == null) return null;
        try {
            return Resource.with(
                    HashingUtils.checkSum(file.getBytes()),
                    file.getBytes(),
                    file.getContentType(),
                    file.getOriginalFilename()
            );


        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

