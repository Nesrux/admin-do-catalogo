package com.nesrux.admin.catalogo.domain.video;


import com.nesrux.admin.catalogo.domain.video.Resource.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class ResourceTest {
    @Test
    public void givenAvalidParams_whenCallsNewResource_thenInstantiate() {
        //given
        final var expectedContent = "Lorem Ipsum".getBytes(StandardCharsets.UTF_8);
        final var expectedContentType = ".mp4";
        final var expectedName = "archive";
        final var expectedType = Type.BANNER;

        //when
        final var actualResource = Resource.with(expectedContent, expectedContentType, expectedName, expectedType);

        //then
        Assertions.assertNotNull(actualResource);
        Assertions.assertEquals(expectedContent, actualResource.content());
        Assertions.assertEquals(expectedContentType, actualResource.contentType());
        Assertions.assertEquals(expectedName, actualResource.name());
        Assertions.assertEquals(expectedType, actualResource.type());
    }


    @Test
    public void givenAnInvalidName_whenCallsNewResource_throwsException() {
        //given
        final var expectedContent = "Lorem Ipsum".getBytes(StandardCharsets.UTF_8);
        final var expectedContentType = ".mp4";
        final var expectedName = "archive";
        final var expectedType = Type.BANNER;

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(null, expectedContentType, expectedName, expectedType));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedContent, null, expectedName, expectedType));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedContent, expectedContentType, null, expectedType));

        Assertions.assertThrows(NullPointerException.class, () ->
                Resource.with(expectedContent, expectedContentType, expectedName, null));
    }
}