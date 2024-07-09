package com.nesrux.admin.catalogo.application.video.media.get;

import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.video.MediaResourceGateway;
import com.nesrux.admin.catalogo.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetMediaUseCase useCase;
    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(mediaResourceGateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCommand_thenReturnResource() {
        //given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.ramdomVideoMediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(expectedResource));

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        //when
        final var actualResult = this.useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualResult);
        Assertions.assertEquals(expectedResource.content(), actualResult.content());
        Assertions.assertEquals(expectedResource.contentType(), actualResult.contentType());
        Assertions.assertEquals(expectedResource.name(), actualResult.name());

    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_thenReturNotFoundException() {
        //given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.ramdomVideoMediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        //then
        final var actualResult = Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(aCommand));

    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExistis_thenReturNotFoundException() {
        //given
        final var expectedId = VideoID.unique();
        final var expectedErrorMessageError = "Media type ERROR STRING doesn't exists";

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), "ERROR STRING");

        //when
        final var actualExceptions = Assertions.assertThrows(NotFoundException.class,
                () -> this.useCase.execute(aCommand));

        //then
        Assertions.assertEquals(expectedErrorMessageError, actualExceptions.firstErrorMessage());

    }


}
