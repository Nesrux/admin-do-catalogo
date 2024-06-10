package com.nesrux.admin.catalogo.application.castmember.create;

import com.nesrux.admin.catalogo.Fixture;
import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateCastMemberUseCaseIT {
    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberGateway gateway;


    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);


        //when
        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());


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
    }
}
