package com.nesrux.admin.catalogo.application.video.create;

import com.nesrux.admin.catalogo.application.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.domain.video.Resource.Type;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.*;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class CreateVideoUSeCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;
    @Mock
    private CategoryGateway categoryGateway;
    @Mock
    private CastMemberGateway castMemberGateway;
    @Mock
    private GenreGateway genreGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
                videoGateway,
                categoryGateway,
                castMemberGateway,
                genreGateway,
                mediaResourceGateway);
    }

    @Test
    public void givenAvalidCommand_whenCallsCreateVideo_shouldReturnVideoID() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of(
                Fixture.Categories.categories().getId()
        );
        final var expectedGenres = Set.<GenreID>of(
                Fixture.Genres.castMembers().getId()
        );
        final var expectedCastMember = Set.<CastMemberID>of(
                Fixture.CastMembers.castMembers().getId()
        );
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                asString(expectedCategories),
                asString(expectedGenres),
                asString(expectedCastMember),
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        mockImageMedia();
        mockAudioVideoMedia();

        when(categoryGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedGenres));
        when(castMemberGateway.existsByIds(any()))
                .thenReturn(new ArrayList<>(expectedCastMember));
        when(videoGateway.create(any()))
                .thenAnswer(returnsFirstArg());
        //when
        final var actualResult = useCase.execute(aCommand);

        //then
        verify(videoGateway).create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedCastMember, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    private void mockImageMedia() {
        when(mediaResourceGateway.storeImage(any(), any()))
                .thenAnswer(t -> {
                    final var resouce = t.getArgument(1, Resource.class);
                    return ImageMedia.with(UUID.randomUUID().toString(), resouce.name(), "/img");
                });
    }

    private void mockAudioVideoMedia() {
        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenAnswer(t -> {
                    final var resouce = t.getArgument(1, Resource.class);
                    return AudioVideoMedia.with(
                            UUID.randomUUID().toString(),
                            resouce.name(),
                            "/img",
                            "",
                            MediaStatus.PENDING);
                });
    }


}
