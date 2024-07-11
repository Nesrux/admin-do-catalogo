package com.nesrux.admin.catalogo.application.video.media.update;

import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.application.video.media.upload.DefaultUploadMediaUseCase;
import com.nesrux.admin.catalogo.application.video.media.upload.UploadMediaCommand;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.video.MediaResourceGateway;
import com.nesrux.admin.catalogo.domain.video.VideoGateway;
import com.nesrux.admin.catalogo.domain.video.VideoMediaType;
import com.nesrux.admin.catalogo.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class UploadMediaUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultUploadMediaUseCase useCase;
    @Mock
    private MediaResourceGateway mediaResourceGateway;
    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway, videoGateway);
    }


    @Test
    public void givenCMDToUpload_whenIsValid_ShouldUpdateVideoMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo -> Objects.equals(expectedMedia, actualVideo.getVideo()
                .get()) && actualVideo.getTrailer().isEmpty() && actualVideo.getBanner()
                .isEmpty() && actualVideo.getThumbnail().isEmpty() && actualVideo.getThumbnailHalf().isEmpty()));

    }


    @Test
    public void givenCMDToUpload_whenIsValid_ShouldUpdateTrailerMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));
        when(mediaResourceGateway.storeAudioVideo(any(), any())).thenReturn(expectedMedia);

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when

        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo -> Objects.equals(expectedMedia, actualVideo.getTrailer()
                .get()) && actualVideo.getVideo().isEmpty() && actualVideo.getBanner()
                .isEmpty() && actualVideo.getThumbnail().isEmpty() && actualVideo.getThumbnailHalf().isEmpty()));
    }

    @Test
    public void givenCMDToUpload_whenIsValid_ShouldUpdateBannerMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when

        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo -> Objects.equals(expectedMedia, actualVideo.getBanner()
                .get()) && actualVideo.getTrailer().isEmpty() && actualVideo.getVideo()
                .isEmpty() && actualVideo.getThumbnail().isEmpty() && actualVideo.getThumbnailHalf().isEmpty()));

    }

    @Test
    public void givenCMDToUpload_whenIsValid_ShouldUpdateThumbnailMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when

        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo -> Objects.equals(expectedMedia, actualVideo.getThumbnail()
                .get()) && actualVideo.getTrailer().isEmpty() && actualVideo.getBanner()
                .isEmpty() && actualVideo.getVideo().isEmpty() && actualVideo.getThumbnailHalf().isEmpty()));

    }

    @Test
    public void givenCMDToUpload_whenIsValid_ShouldUpdateThumbnailHalfAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));
        when(mediaResourceGateway.storeImage(any(), any())).thenReturn(expectedMedia);

        when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when

        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));

        verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

        verify(videoGateway, times(1)).update(argThat(actualVideo -> Objects.equals(expectedMedia, actualVideo.getThumbnailHalf()
                .get()) && actualVideo.getTrailer().isEmpty() && actualVideo.getBanner()
                .isEmpty() && actualVideo.getThumbnail().isEmpty() && actualVideo.getVideo().isEmpty()));

    }

    @Test
    public void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        //given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any())).thenReturn(Optional.empty());


        final var aCommand = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when

        final var actualOutput = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

    }
}
