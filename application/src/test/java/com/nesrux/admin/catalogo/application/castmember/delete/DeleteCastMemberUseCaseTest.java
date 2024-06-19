package com.nesrux.admin.catalogo.application.castmember.delete;

import com.nesrux.admin.catalogo.application.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIT() {
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aMember.getId();
        doNothing().when(
                castMemberGateway).deleteById(any());
        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(castMemberGateway).deleteById(eq(expectedId));

    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        //given
        final var expectedId = CastMemberID.from("123");
        doNothing().when(
                castMemberGateway).deleteById(any());
        //when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(castMemberGateway).deleteById(eq(expectedId));

    }

    @Test
    void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReciveException() {
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var expectedId = aMember.getId();
        doThrow(new IllegalStateException("Gateway Error"))
                .when(castMemberGateway).deleteById(any());
        //when
        Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        //then
        verify(castMemberGateway, times(1)).deleteById(eq(expectedId));

    }
}
