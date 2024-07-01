package com.nesrux.admin.catalogo.infrastructure.video;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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


    private CastMember joao;
    private CastMember catarina;

    private Category aulas;
    private Category lives;

    private Genre tech;
    private Genre business;

    @BeforeEach
    public void setUp() {
        joao = castMemberGateway.create(Fixture.CastMembers.joao());
        catarina = castMemberGateway.create(Fixture.CastMembers.catarina());

        aulas = categoryGateway.create(Fixture.Categories.aulas());
        lives = categoryGateway.create(Fixture.Categories.lives());

        tech = genreGateway.create(Fixture.Genres.tech());
        business = genreGateway.create(Fixture.Genres.business());
    }


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

        final var aVideo = Video.newVideo(
                expectedTitle, expectedDescription, expectedLaunchYear,
                expectedDuration, expectedOpened, expectedPublished,
                expectedRating, expectedCategories, expectedGenres, expectedCastMember
        );


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
        Assertions.assertTrue(actualResult.getVideo().isEmpty());
        Assertions.assertTrue(actualResult.getTrailer().isEmpty());
        Assertions.assertTrue(actualResult.getBanner().isEmpty());
        Assertions.assertTrue(actualResult.getThumbnail().isEmpty());
        Assertions.assertTrue(actualResult.getThumbnailHalf().isEmpty());

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
        Assertions.assertNull(persistedVideo.getVideo());
        Assertions.assertNull(persistedVideo.getTrailer());
        Assertions.assertNull(persistedVideo.getBanner());
        Assertions.assertNull(persistedVideo.getThumbnail());
        Assertions.assertNull(persistedVideo.getThumbnailHalf());
    }

    @Test
    @Transactional
    public void givenAValidVideo_whenCallsUpdate_shouldPersisted() {
        //given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.Videos.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
                Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));


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


        final var updatedVideo = Video.with(aVideo)
                .update(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedCastMember
                ).setVideo(expectedVideo)
                .setTrailer(expectedTrailer)
                .setBanner(expectedBanner)
                .setThumbnail(expectedThumb)
                .setThumbnailHalf(expectedThumbHalf);


        //when
        Assertions.assertEquals(this.videoRepository.count(), 1);

        final var actualResult = videoGateway.update(updatedVideo);

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
        Assertions.assertNotNull(actualResult.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualResult.getUpdatedAt()));

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
        Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

    }


    @Test
    public void givenAValidIdWithVideoPrepersisted_whenCallsDelete_shouldDeletedId() {
        //given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.Videos.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
                Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));

        final var expectedId = aVideo.getId();
        Assertions.assertEquals(this.videoRepository.count(), 1);
        //when
        Assertions.assertDoesNotThrow(() -> this.videoGateway.deleteById(expectedId));

        //then

        Assertions.assertEquals(this.videoRepository.count(), 0);
    }


    @Test
    public void givenAnInvalidVideoId_whenCallsDelete_shouldBeOk() {
        //given
        final var aVideo = videoGateway.create(Video.newVideo(
                Fixture.Videos.title(),
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
                Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.<CategoryID>of(),
                Set.<GenreID>of(),
                Set.<CastMemberID>of()
        ));

        final var expectedId = aVideo.getId();
        Assertions.assertEquals(this.videoRepository.count(), 1);
        //when
        Assertions.assertDoesNotThrow(() -> this.videoGateway.deleteById(VideoID.unique()));

        //then
        Assertions.assertEquals(this.videoRepository.count(), 1);
    }

    @Test
    public void givenAValidVideoId_whenCallsFindByid_shouldReturnit() {
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


        final var aVideo = this.videoGateway.create(
                Video.newVideo(
                                expectedTitle, expectedDescription, expectedLaunchYear,
                                expectedDuration, expectedOpened, expectedPublished,
                                expectedRating, expectedCategories, expectedGenres, expectedCastMember
                        ).setVideo(expectedVideo)
                        .setTrailer(expectedTrailer)
                        .setBanner(expectedBanner)
                        .setThumbnail(expectedThumb)
                        .setThumbnailHalf(expectedThumbHalf)
        );

        final var expectedId = aVideo.getId();
        Assertions.assertEquals(this.videoRepository.count(), 1);

        //when
        final var actualResult = videoGateway.findById(expectedId).get();

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

    }


    @Test
    public void givenAnInvalidVideoID_whenCallsFindByid_shouldEmpty() {
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


        this.videoGateway.create(
                Video.newVideo(
                        expectedTitle, expectedDescription, expectedLaunchYear,
                        expectedDuration, expectedOpened, expectedPublished,
                        expectedRating, expectedCategories, expectedGenres, expectedCastMember
                )
        );

        Assertions.assertEquals(this.videoRepository.count(), 1);

        //when
        final var actualVideo = videoGateway.findById(VideoID.unique());

        //then
        Assertions.assertTrue(actualVideo.isEmpty());

    }
    @Test
    public void givenEmptyParams_whenCallFindAll_shouldReturnAllList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 4;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @Test
    public void givenAValidCategory_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(aulas.getId()),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

        Assertions.assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
    }

    @Test
    public void givenAValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(joao.getId()),
                Set.of(),
                Set.of()
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
        Assertions.assertEquals("System Design no Mercado Livre na prática", actualPage.items().get(1).title());
    }

    @Test
    public void givenAValidGenre_whenCallFindAll_shouldReturnFilteredList() {
        // given
        mockVideos();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                Set.of(),
                Set.of(),
                Set.of(business.getId())
        );

        // when
        final var actualPage = videoGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());

        Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    }


    private void mockVideos() {
        videoGateway.create(Video.newVideo(
                "System Design no Mercado Livre na prática",
                Fixture.Videos.description(),
                Year.of(2022),
               Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.of(lives.getId()),
                Set.of(tech.getId()),
                Set.of(joao.getId(), catarina.getId())
        ));

        videoGateway.create(Video.newVideo(
                "Não cometa esses erros ao trabalhar com Microsserviços",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
               Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.of(),
                Set.of(),
                Set.of()
        ));

        videoGateway.create(Video.newVideo(
                "21.1 Implementação dos testes integrados do findAll",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
               Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.of(aulas.getId()),
                Set.of(tech.getId()),
                Set.of(catarina.getId())
        ));

        videoGateway.create(Video.newVideo(
                "Aula de empreendedorismo",
                Fixture.Videos.description(),
                Year.of(Fixture.Videos.year()),
               Fixture.Videos.randomDuration(),
                Fixture.bool(),
                Fixture.bool(),
                Fixture.Videos.randomRating(),
                Set.of(aulas.getId()),
                Set.of(business.getId()),
                Set.of(joao.getId())
        ));
    }

}
