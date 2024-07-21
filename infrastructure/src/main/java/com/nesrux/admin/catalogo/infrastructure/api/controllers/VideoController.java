package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.video.create.CreateVideoCommand;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.GetVideoByIdUseCase;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.infrastructure.api.VideoApi;
import com.nesrux.admin.catalogo.infrastructure.utils.HashingUtils;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.CreateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.VideoResponse;
import com.nesrux.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoApi {
    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
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

