package com.nesrux.admin.catalogo.application.castmember.retrive.get;

import com.nesrux.admin.catalogo.application.Fixture;
import com.nesrux.admin.catalogo.application.UseCaseTest;
import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetMemberByIdUseCaseTest extends UseCaseTest {
    @Mock
    private CastMemberGateway castMemberGateway;

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase usecase;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAvalidID_whenCallsGetCastMemberById_shouldReturnIT() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));
        //when
        final var actualOutput = usecase.execute(expectedId.getValue());

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

        verify(castMemberGateway).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidID_whenCallsGetCastMemberByIdAndDoesNotExists_shouldReturnNotFoundException() {
        //given
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());
        //when
        final var expectedException = Assertions.assertThrows(NotFoundException.class,
                () -> usecase.execute(expectedId.getValue()));

        //then
        Assertions.assertNotNull(expectedException);
        Assertions.assertEquals(expectedErrorMessage, expectedException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }
}
