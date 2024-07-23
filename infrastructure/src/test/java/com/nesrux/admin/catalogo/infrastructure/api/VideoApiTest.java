package com.nesrux.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nesrux.admin.catalogo.ControllerTest;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoCommand;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoOutput;
import com.nesrux.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.nesrux.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.nesrux.admin.catalogo.application.video.media.get.GetMediaUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.GetVideoByIdUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.get.VideoOutput;
import com.nesrux.admin.catalogo.application.video.retrive.list.ListVideosUseCase;
import com.nesrux.admin.catalogo.application.video.retrive.list.VideoListOutput;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.nesrux.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryID;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.utils.CollectionsUtil;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.video.*;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.CreateVideoRequest;
import com.nesrux.admin.catalogo.infrastructure.video.models.api.UpdateVideoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoApi.class)
class VideoApiTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideosUseCase listVideosUseCase;

    @Test
    void givenAvlidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        //given
        final var expectedID = VideoID.unique();
        final var catarina = Fixture.CastMembers.catarina().getId();
        final var tech = Fixture.Genres.tech().getId();
        final var aulas = Fixture.Categories.aulas().getId();
        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.of(aulas.getValue());
        final var expectedGenres = Set.of(tech.getValue());
        final var expectedCastMember = Set.of(catarina.getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "trailer/mp4", "TRAILER".getBytes());

        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());

        final var expectedThumb =
                new MockMultipartFile("thumb_file", "thumb.jpg", "image/jpg", "THUMBNAIL".getBytes());

        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbHalf.jpg", "image/jpg", "THUMBNAILHALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedID.getValue()));
        //when
        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("categories_id", aulas.getValue())
                .param("genres_id", tech.getValue())
                .param("cast_members_id", catarina.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedID.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedID.getValue())));

        //then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedCastMember, actualCmd.members());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.getOriginalFilename(), actualCmd.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.getThumbnailHalf().get().name());
    }

    @Test
    void givenAvalidCommdan_whenCalssCreatePartial_shouldReturnId() throws Exception {
        //given
        final var expectedID = VideoID.unique();
        final var catarina = Fixture.CastMembers.catarina().getId();
        final var tech = Fixture.Genres.tech().getId();
        final var aulas = Fixture.Categories.aulas().getId();

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.of(aulas.getValue());
        final var expectedGenres = Set.of(tech.getValue());
        final var expectedCastMembers = Set.of(catarina.getValue());

        final var aCommand = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedID.getValue()));

        //when
        final var aRequest = post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(aCommand));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedID.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedID.getValue())));
        //then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedCastMembers, actualCmd.members());

        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    void givenAvaidId_whenCallsGetById_shouldReturnVideo() throws Exception {
        //given
        final var catarina = Fixture.CastMembers.catarina().getId();
        final var tech = Fixture.Genres.tech().getId();
        final var aulas = Fixture.Categories.aulas().getId();

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.of(aulas.getValue());
        final var expectedGenres = Set.of(tech.getValue());
        final var expectedCastMembers = Set.of(catarina.getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        CollectionsUtil.mapTo(expectedCategories, CategoryID::from),
                        CollectionsUtil.mapTo(expectedGenres, GenreID::from),
                        CollectionsUtil.mapTo(expectedCastMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbHalfMedia(expectedThumbHalf);
        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));
        //when
        final var aRequest = get("/videos/" + expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList(expectedCastMembers))));

    }

    @Test
    void givenAValidCommand_whenCalssUpdateVideo_ShouldReturnVideo() throws Exception {
        //given
        final var expectedID = VideoID.unique();
        final var catarina = Fixture.CastMembers.catarina().getId();
        final var tech = Fixture.Genres.tech().getId();
        final var aulas = Fixture.Categories.aulas().getId();

        final var expectedTitle = Fixture.Videos.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.of(aulas.getValue());
        final var expectedGenres = Set.of(tech.getValue());
        final var expectedCastMembers = Set.of(catarina.getValue());

        final var aCommand = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(updateVideoUseCase.execute(any()))
                .thenReturn(new UpdateVideoOutput(expectedID.getValue()));

        //when
        final var aRequest = put("/videos/" + expectedID.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(aCommand));

        this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/videos/" + expectedID.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedID.getValue())));
        //then
        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        verify(updateVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedCastMembers, actualCmd.members());

        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalid_whenCalssUpdate_shouldReturnNotifications() throws Exception {
        //given
        final var expectedID = VideoID.unique();
        final var catarina = Fixture.CastMembers.catarina().getId();
        final var tech = Fixture.Genres.tech().getId();
        final var aulas = Fixture.Categories.aulas().getId();

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.Videos.year());
        final var expectedDuration = Fixture.Videos.randomDuration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.randomRating();
        final var expectedCategories = Set.of(aulas.getValue());
        final var expectedGenres = Set.of(tech.getValue());
        final var expectedCastMembers = Set.of(catarina.getValue());

        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = new UpdateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear.getValue(),
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        when(updateVideoUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //when
        final var aRequest = put("/videos/" + expectedID.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(aCommand));

        final var response = this.mvc.perform(aRequest);
        //then
        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() throws Exception {
        //given
        final var expectedID = VideoID.unique();

        doNothing()
                .when(deleteVideoUseCase).execute(any());
        //when
        final var aRequest = delete("/videos/" + expectedID.getValue());
        final var aResponse = this.mvc.perform(aRequest);
        //then
        aResponse.andExpect(status().isNoContent());
        verify(deleteVideoUseCase).execute(eq(expectedID.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideos_shouldReturnPagination() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.Videos.randomVideo());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        Assertions.assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualQuery.castMembers());
        Assertions.assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
    }

    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_shouldReturnPagination() throws Exception {
        // given
        final var aVideo = new VideoPreview(Fixture.Videos.randomVideo());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
                .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedTerms, actualQuery.terms());
        Assertions.assertTrue(actualQuery.categories().isEmpty());
        Assertions.assertTrue(actualQuery.castMembers().isEmpty());
        Assertions.assertTrue(actualQuery.genres().isEmpty());
    }

}