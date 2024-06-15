package com.nesrux.admin.catalogo.domain.video;

import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.genre.GenreID;
import com.nesrux.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest {

    @Test
    void givenAValidParams_whenCallsNewVideo_ShouldInstantiate() {
        //given
        final var expectedTitle = "GOKU Morre!";
        final var expectedDescription = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et +
                s egestas maecenas pharetra convallisAdipiscing elit duis
                """;

        final var expectedLaunchedAt = Year.of(2023);
        final var expectedDurarion = 180.0;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryId.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedCastMembers = Set.of(CastMemberID.unique());

        //when
        final var actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDurarion,
                expectedRating,
                expectedOpened,
                expectedPublished,
                expectedCategories,
                expectedGenres,
                expectedCastMembers
        );

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDurarion, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedCastMembers, actualVideo.getCastMembers());

        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty()); // getTrailer
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}
