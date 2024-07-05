package com.nesrux.admin.catalogo.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GCStorageServiceTest {

    private GCStorageService target;
    private Storage storage;
    private String bucket = "test-bucket";

    @BeforeEach
    public void setup() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(this.bucket, this.storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldPersistIT() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        //when
        this.target.store(expectedName, expectedResource);

        //then
        final var captor = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(captor.capture(), eq(expectedResource.content()));

        final var actualBlob = captor.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedName, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedResource.checkSum(), actualBlob.getCrc32cToHexString());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetriveIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        //when
        final var actualResorce = this.target.get(expectedName).get();

        //then
        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertEquals(expectedResource, actualResorce);

    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        final var blob = mockBlob(expectedName, expectedResource);
        doReturn(null).when(storage).get(anyString(), anyString());

        //when
        final var actualResorce = this.target.get(expectedName);

        //then
        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertTrue(actualResorce.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_shouldRetriveAll() {
        //given
        final var expectedPrefix = "Media_";

        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();

        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        final var blobVideo = mockBlob(expectedNameVideo, expectedVideo);
        final var blobBanner = mockBlob(expectedNameBanner, expectedBanner);

        final var page = Mockito.mock(Page.class);

        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();

        doReturn(page).when(storage).list(anyString(), any());

        //when
        final var actualResorce = this.target.list(expectedPrefix);

        //then
        verify(storage, times(1)).list(eq(this.bucket), eq(Storage.BlobListOption.prefix(expectedPrefix)));


        Assertions.assertTrue(
                expectedResources.size() == actualResorce.size()
                        && expectedResources.containsAll(actualResorce)
        );
    }


    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        //given
        final var expectedPrefix = "Media_";

        final var expectedNameVideo = expectedPrefix + IdUtils.uuid();
        final var expectedNameBanner = expectedPrefix + IdUtils.uuid();

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        //when
        this.target.deleteAll(expectedResources);

        //then
        final var capter = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(capter.capture());

        final var actualResources = ((List<BlobId>) capter.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        Assertions.assertTrue(
                expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources)
        );


    }

    private Blob mockBlob(final String name, final Resource expectedResource) {
        final var blob = Mockito.mock(Blob.class);

        when(blob.getBlobId()).thenReturn(BlobId.of(this.bucket, name));
        when(blob.getCrc32cToHexString()).thenReturn(expectedResource.checkSum());
        when(blob.getContent()).thenReturn(expectedResource.content());
        when(blob.getContent()).thenReturn(expectedResource.content());
        when(blob.getContentType()).thenReturn(expectedResource.contentType());
        when(blob.getName()).thenReturn(expectedResource.name());

        return blob;
    }

}