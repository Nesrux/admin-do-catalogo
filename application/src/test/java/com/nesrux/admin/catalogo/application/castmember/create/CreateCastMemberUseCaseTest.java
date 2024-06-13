package com.nesrux.admin.catalogo.application.castmember.create;

import com.nesrux.admin.catalogo.application.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway gateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        when(gateway.create(any()))
                .thenAnswer(returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(gateway).create(argThat(aMember ->
                Objects.nonNull(aMember.getId())
                        && Objects.equals(expectedName, aMember.getName())
                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldThrowsNotifications() {
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var expectedError = Assertions.assertThrows(NotificationException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertNotNull(expectedError);
        Assertions.assertEquals(expectedErrorCount, expectedError.size());
        Assertions.assertEquals(expectedErrorMessage, expectedError.firstErrorMessage());
        verify(gateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsCreateCastMember_shouldThrowsNotifications() {
        //given
        final var expectedName = "  ";
        final var expectedType = Fixture.CastMember.type();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var expectedError = Assertions.assertThrows(NotificationException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertNotNull(expectedError);
        Assertions.assertEquals(expectedErrorCount, expectedError.size());
        Assertions.assertEquals(expectedErrorMessage, expectedError.firstErrorMessage());
        verify(gateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsCreateCastMember_shouldThrowsNotifications() {
        //given
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        //when
        final var expectedError = Assertions.assertThrows(NotificationException.class,
                () -> useCase.execute(aCommand));

        Assertions.assertNotNull(expectedError);
        Assertions.assertEquals(expectedErrorCount, expectedError.size());
        Assertions.assertEquals(expectedErrorMessage, expectedError.firstErrorMessage());
        verify(gateway, times(0)).create(any());
    }
}
