package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.application.video.create.CreateVideoCommand;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.infrastructure.api.VideoApi;
import com.nesrux.admin.catalogo.infrastructure.utils.HashingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoApi {
    private final CreateVideoUseCase useCase;

    public VideoController(final CreateVideoUseCase useCase) {
        this.useCase = Objects.requireNonNull(useCase);
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

        final var output = this.useCase.execute(aCommd);
        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
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

