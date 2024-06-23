package com.nesrux.admin.catalogo.application.video.retrive.list;

import com.nesrux.admin.catalogo.application.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.application.genre.retrive.list.GenreListOutput;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.domain.pagination.SearchQuery;
import com.nesrux.admin.catalogo.domain.video.Video;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import com.nesrux.admin.catalogo.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ListVideoUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultListVideoUseCase useCase;
    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    public void givenAValidQuery_whenCallsListVideos_ShouldReturnVideos() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var videos = List.of(
                Fixture.Videos.randomVideo(),
                Fixture.Videos.randomVideo()
        );

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );
        //when
        final var actualOutput = usecase.execute(aQuery);

        //then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPage, actualOutput.perpage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
        // given
        final var videos = List.<Video>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<VideoListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndGatewayThrowsRandomError_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(genreGateway, times(1)).findAll(eq(aQuery));
    }
}
