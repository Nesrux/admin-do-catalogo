package com.nesrux.admin.catalogo.domain.video;


import com.nesrux.admin.catalogo.domain.UnitTest;
import com.nesrux.admin.catalogo.domain.resource.Resource;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class ResourceTest  extends UnitTest {
    @Test
    public void givenAvalidParams_whenCallsNewResource_thenInstantiate() {
        //given
        final var expectedContent = "Lorem Ipsum".getBytes(StandardCharsets.UTF_8);
        final var expectedChecksum = IdUtils.uuid();
        final var expectedContentType = ".mp4";
        final var expectedName = "archive";
        final var expectedType = VideoMediaType.BANNER;

        //when
        final var actualResource = Resource.with(expectedChecksum, expectedContent, expectedContentType, expectedName);

        //then
        Assertions.assertNotNull(actualResource);
        Assertions.assertEquals(expectedContent, actualResource.content());
        Assertions.assertEquals(expectedContentType, actualResource.contentType());
        Assertions.assertEquals(expectedName, actualResource.name());
        Assertions.assertEquals(expectedChecksum, actualResource.checkSum());
    }


    @Test
    public void givenAnInvalidName_whenCallsNewResource_throwsException() {
        //given
        final var expectedContent = "Lorem Ipsum".getBytes(StandardCharsets.UTF_8);
        final var expectedCheckSum = IdUtils.uuid();
        final var expectedContentType = ".mp4";
        final var expectedName = "archive";
        final var expectedType = VideoMediaType.BANNER;

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(null, expectedContent, expectedName, expectedName));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedCheckSum, null, expectedName, expectedName));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedCheckSum, expectedContent, null, expectedName));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedCheckSum, expectedContent, expectedName, null));
    }
}