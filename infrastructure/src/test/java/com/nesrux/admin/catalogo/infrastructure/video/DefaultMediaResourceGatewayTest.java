package com.nesrux.admin.catalogo.infrastructure.video;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.infrastructure.services.StorageService;
import com.nesrux.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@IntegrationTest
public class DefaultMediaResourceGatewayTest {
    @Autowired
    private MediaResourceGateway mediaResourceGateway;

    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        castService().reset();
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncondedLocation = "";
        //when
        final var actualMedia =
                this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia);
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checkSum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncondedLocation, actualMedia.encodedLocation());

        final var actualStored = castService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        //when
        final var actualMedia =
                this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia);
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checkSum(), actualMedia.checksum());

        final var actualStored = castService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        //given
        final var videoId1 = VideoID.unique();
        final var videoId2 = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoId1.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoId1.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoId1.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoId2.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoId2.getValue(), VideoMediaType.TRAILER.name()));

        toBeDeleted.forEach(id -> storageService.store(id, Fixture.Videos.resource(Fixture.Videos.ramdomVideoMediaType())));
        expectedValues.forEach(id -> storageService.store(id, Fixture.Videos.resource(Fixture.Videos.ramdomVideoMediaType())));

        Assertions.assertEquals(5, castService().storage().size());
        //when
        this.mediaResourceGateway.clearResources(videoId1);
        //then
        Assertions.assertEquals(2, castService().storage().size());

        final var actualKeys = castService().storage().keySet();
        Assertions.assertTrue(expectedValues.size() == actualKeys.size()
                && expectedValues.containsAll(actualKeys));
    }

    private InMemoryStorageService castService() {
        return (InMemoryStorageService) storageService;
    }
}