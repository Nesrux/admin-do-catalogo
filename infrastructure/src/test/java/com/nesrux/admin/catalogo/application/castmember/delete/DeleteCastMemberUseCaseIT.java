package com.nesrux.admin.catalogo.application.castmember.delete;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {
    @Autowired
    private DeleteCastMemberUseCase useCase;
    @Autowired
    private CastMemberGateway castMemberGateway;


    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIT() {
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aMember.getId();

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then

    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        //given
        final var expectedId = CastMemberID.from("123");

        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then

    }
}
