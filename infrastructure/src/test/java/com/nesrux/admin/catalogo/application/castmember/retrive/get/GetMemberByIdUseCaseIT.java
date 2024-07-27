package com.nesrux.admin.catalogo.application.castmember.retrive.get;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.nesrux.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class GetMemberByIdUseCaseIT {
    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private GetCastMemberUseCase usecase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    void givenAvalidID_whenCallsGetCastMemberById_shouldReturnIT() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        //when
        final var actualOutput = usecase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

    }

    @Test
    void givenAnInvalidID_whenCallsGetCastMemberByIdAndDoesNotExists_shouldReturnNotFoundException() {
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";


        //when
        final var expectedException = Assertions.assertThrows(NotFoundException.class,
                () -> usecase.execute(expectedId.getValue()));

        //then
        Assertions.assertNotNull(expectedException);
        Assertions.assertEquals(expectedErrorMessage, expectedException.getMessage());


    }
}
