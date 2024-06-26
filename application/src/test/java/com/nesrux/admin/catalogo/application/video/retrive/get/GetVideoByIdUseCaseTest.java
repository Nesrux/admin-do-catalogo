package com.nesrux.admin.catalogo.application.video.retrive.get;

import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.domain.video.Resource.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCalssGetVideo_ShouldReturnIt() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.<CategoryID>of(Fixture.Categories.randomCategory().getId());
        final var expectedGenres = Set.<GenreID>of(Fixture.Genres.randomGenre().getId());
        final var expectedCastMember = Set.<CastMemberID>of(Fixture.CastMembers.randomMember().getId());
        final var expectedVideo = audioVideo(Type.VIDEO);
        final var expectedBanner = imageMedia(Type.BANNER);
        final var expectedTrailer = audioVideo(Type.TRAILER);
        final var expectedThumb = imageMedia(Type.THUMBNAIL);
        final var expectedThumbHalf = imageMedia(Type.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMember)
                .setVideo(expectedVideo)
                .setBanner(expectedBanner)
                .setTrailer(expectedTrailer)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);

        final var anId = aVideo.getId();
        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        //when

        final var actualVideo = this.useCase.execute(anId.getValue());
        //then

        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(asString(expectedCategories), actualVideo.categories());
        Assertions.assertEquals(asString(expectedGenres), actualVideo.genres());
        Assertions.assertEquals(asString(expectedCastMember), actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.treiler());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumb, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());

    }

    @Test
    public void givenAnInvalidId_whenCallsgetVideo_shouldReturnNotFound() {
        //given
        final var expectedErrorMessage = "Video with ID 123 was not found";
        final var expectedId = VideoID.from("123");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());
        //when
        final var expectedError = Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, expectedError.getMessage());
    }

    private AudioVideoMedia audioVideo(final Type type) {
        final var id = IdUtils.uuid();
        return AudioVideoMedia.with(
                id,
                "abc",
                type.name().toLowerCase(),
                "/videos" + id,
                "",
                MediaStatus.PENDING);
    }

    private ImageMedia imageMedia(final Type type) {
        final var checkSum = IdUtils.uuid();
        return ImageMedia.with(
                checkSum,
                type.name().toLowerCase(),
                "/images/" + checkSum
        );
    }
}
