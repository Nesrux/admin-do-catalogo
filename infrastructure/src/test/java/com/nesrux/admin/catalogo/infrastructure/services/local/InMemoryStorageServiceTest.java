package com.nesrux.admin.catalogo.infrastructure.services.local;

import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InMemoryStorageServiceTest {

    private final InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setup() {
        this.target.reset();
    }

    @Test
    public void givenAvalidResource_whenCallsStore_shouldStoreIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        //when
        target.store(expectedName, expectedResource);

        //then
        Assertions.assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenAvalidResource_whenCallsGet_shouldRetrieve() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        target.store(expectedName, expectedResource);

        //when
        final var actualResource = target.get(expectedName).get();

        //then
        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenAnInvalidResource_whenCallsGet_ShouldReturnEmpty() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        target.store(expectedName, expectedResource);

        //when
        final var actualResource = target.get("Resource name");

        //then
        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        final var actualContent = target.list("it");

        Assertions.assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        target.deleteAll(expectedIds);

        Assertions.assertTrue(this.target.storage().isEmpty());
    }
}