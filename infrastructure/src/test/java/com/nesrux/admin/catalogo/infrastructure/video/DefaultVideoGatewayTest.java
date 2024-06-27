package com.nesrux.admin.catalogo.infrastructure.video;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.video.AudioVideoMedia;
import com.nesrux.admin.catalogo.domain.video.ImageMedia;
import com.nesrux.admin.catalogo.domain.video.Video;
import com.nesrux.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Set;

@IntegrationTest
public class DefaultVideoGatewayTest {
    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        //given
        final var joao = this.castMemberGateway.create(Fixture.CastMembers.joao());
        final var aulas = this.categoryGateway.create(Fixture.Categories.aulas());
        final var tech = this.genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.<CategoryID>of(aulas.getId());
        final var expectedGenres = Set.<GenreID>of(tech.getId());
        final var expectedCastMember = Set.<CastMemberID>of(joao.getId());

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "Thumb", "/media/Thumb");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "ThumbHalf", "/media/ThumbHalf");


        final var aVideo = Video.newVideo(
                        expectedTitle, expectedDescription, expectedLaunchYear,
                        expectedDuration, expectedOpened, expectedPublished,
                        expectedRating, expectedCategories, expectedGenres, expectedCastMember
                ).setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);


        //when
        Assertions.assertEquals(this.videoRepository.count(), 0);

        final var actualResult = videoGateway.create(aVideo);

        Assertions.assertEquals(this.videoRepository.count(), 1);

        //then
        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.getId());
        Assertions.assertEquals(expectedTitle, actualResult.getTitle());
        Assertions.assertEquals(expectedDescription, actualResult.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualResult.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualResult.getDuration());
        Assertions.assertEquals(expectedOpened, actualResult.getOpened());
        Assertions.assertEquals(expectedPublished, actualResult.getPublished());
        Assertions.assertEquals(expectedRating, actualResult.getRating());
        Assertions.assertEquals(expectedCategories, actualResult.getCategories());
        Assertions.assertEquals(expectedGenres, actualResult.getGenres());
        Assertions.assertEquals(expectedCastMember, actualResult.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualResult.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualResult.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualResult.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualResult.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualResult.getThumbnailHalf().get().name());

        final var persistedVideo = this.videoRepository.findById(aVideo.getId().getValue()).get();


        Assertions.assertNotNull(persistedVideo);
        Assertions.assertNotNull(persistedVideo.getId());
        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYerarLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesId());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresId());
        Assertions.assertEquals(expectedCastMember, persistedVideo.getMembersId());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());

    }

    @Test
    @Transactional
    public void givenAValidVideoWhitoutRalations_whenCallsCreate_shouldPersistIt() {
        //given
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedCastMember = Set.<CastMemberID>of();

        final AudioVideoMedia expectedVideo = AudioVideoMedia.with("123", "video", "/media/video");
        final AudioVideoMedia expectedTrailer = AudioVideoMedia.with("123", "trailer", "/media/trailer");
        final ImageMedia expectedBanner = ImageMedia.with("123", "banner", "/media/banner");
        final ImageMedia expectedThumb = ImageMedia.with("123", "Thumb", "/media/Thumb");
        final ImageMedia expectedThumbHalf = ImageMedia.with("123", "ThumbHalf", "/media/ThumbHalf");


        final var aVideo = Video.newVideo(
                        expectedTitle, expectedDescription, expectedLaunchYear,
                        expectedDuration, expectedOpened, expectedPublished,
                        expectedRating, expectedCategories, expectedGenres, expectedCastMember
                ).setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);


        //when
        Assertions.assertEquals(this.videoRepository.count(), 0);

        final var actualResult = videoGateway.create(aVideo);

        Assertions.assertEquals(this.videoRepository.count(), 1);

        //then
        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.getId());
        Assertions.assertEquals(expectedTitle, actualResult.getTitle());
        Assertions.assertEquals(expectedDescription, actualResult.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualResult.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualResult.getDuration());
        Assertions.assertEquals(expectedOpened, actualResult.getOpened());
        Assertions.assertEquals(expectedPublished, actualResult.getPublished());
        Assertions.assertEquals(expectedRating, actualResult.getRating());
        Assertions.assertEquals(expectedCategories, actualResult.getCategories());
        Assertions.assertEquals(expectedGenres, actualResult.getGenres());
        Assertions.assertEquals(expectedCastMember, actualResult.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualResult.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualResult.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualResult.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualResult.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualResult.getThumbnailHalf().get().name());

        final var persistedVideo = this.videoRepository.findById(aVideo.getId().getValue()).get();


        Assertions.assertNotNull(persistedVideo);
        Assertions.assertNotNull(persistedVideo.getId());
        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYerarLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesId());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresId());
        Assertions.assertEquals(expectedCastMember, persistedVideo.getMembersId());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());

    }

}
