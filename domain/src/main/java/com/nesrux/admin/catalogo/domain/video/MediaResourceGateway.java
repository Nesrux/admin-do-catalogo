package com.nesrux.admin.catalogo.domain.video;

import com.nesrux.admin.catalogo.domain.resource.Resource;

import java.util.Optional;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    void clearResources(VideoID anId);

    Optional<Resource> getResource(VideoID anId, VideoMediaType type);
}
