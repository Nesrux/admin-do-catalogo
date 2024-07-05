package com.nesrux.admin.catalogo.infrastructure.video;

import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.infrastructure.configuration.properties.storage.StorageProperties;
import com.nesrux.admin.catalogo.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {
    private final String fileNamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props,
                                       final StorageService storageService) {
        this.fileNamePattern = Objects.requireNonNull(props.filenamePattern());
        this.locationPattern = Objects.requireNonNull(props.locationPattern());
        this.storageService = Objects.requireNonNull(storageService);
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();

        store(filePath, aResource);

        return AudioVideoMedia.with(aResource.checkSum(), aResource.name(), filePath);
    }


    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();

        store(filePath, aResource);

        return ImageMedia.with(aResource.checkSum(), aResource.name(), filePath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    private String fileName(final VideoMediaType aType) {
        return fileNamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoResource aResource) {
        return folder(anId)
                .concat("/")
                .concat(fileName(aResource.type()));
    }

    private void store(String filePath, Resource aResource) {
        this.storageService.store(filePath, aResource);
    }

}
