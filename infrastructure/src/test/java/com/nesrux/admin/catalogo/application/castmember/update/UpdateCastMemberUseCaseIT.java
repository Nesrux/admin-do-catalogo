package com.nesrux.admin.catalogo.application.castmember.update;

import com.nesrux.admin.catalogo.Fixture;
import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberType;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {
    @Autowired
    private CastMemberGateway gateway;

    @Autowired
    private DefaultUpdateCastMemberUsecase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;


    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
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

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);


        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());


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
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);


        //when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotificationExceptions() {
        //given
        final var aMember = CastMember.newMember("Chris Rock", CastMemberType.DIRECTOR);
        final var expectedId = CastMemberID.from("1234");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "CastMember with ID 1234 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCommand));

        //then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());


    }
}
