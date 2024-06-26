package com.nesrux.admin.catalogo.application.castmember.update;

import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCastMemberUseCaseTest extends UseCaseTest {
    @Mock
    private CastMemberGateway gateway;

    @InjectMocks
    private DefaultUpdateCastMemberUsecase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(gateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        when(gateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(gateway).findById(eq(expectedId));

        verify(gateway).update(argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
        ));
    }


    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationExceptions() {
        //given
        final var aMember = CastMember.newMember("Chris Rock", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(gateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

        verify(gateway).findById(eq(expectedId));
        verify(gateway, times(0))
                .update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationExceptions() {
        //given
        final var aMember = CastMember.newMember("Chris Rock", CastMemberType.DIRECTOR);
        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(gateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

        verify(gateway).findById(eq(expectedId));
        verify(gateway, times(0))
                .update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotificationExceptions() {
        //given
        final var aMember = CastMember.newMember("Chris Rock", CastMemberType.DIRECTOR);
        final var expectedId = CastMemberID.from("1234");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedErrorMessage = "CastMember with ID 1234 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(gateway.findById(any()))
                .thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


        verify(gateway).findById(eq(expectedId));
        verify(gateway, times(0))
                .update(any());
    }
}
