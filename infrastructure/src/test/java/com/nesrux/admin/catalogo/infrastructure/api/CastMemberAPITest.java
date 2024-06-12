package com.nesrux.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nesrux.admin.catalogo.ControllerTest;
import com.nesrux.admin.catalogo.Fixture;
import com.nesrux.admin.catalogo.application.castmember.create.CreateCastMemberOutput;
import com.nesrux.admin.catalogo.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.DefaultGetCastMemberByIdUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.get.GetCastMemberUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.list.DefaultListCastMembersUseCase;
import com.nesrux.admin.catalogo.application.castmember.retrive.list.ListCastMembersUseCase;
import com.nesrux.admin.catalogo.application.castmember.update.DefaultUpdateCastMemberUsecase;
import com.nesrux.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.infrastructure.castmember.models.CreateCastMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultUpdateCastMemberUsecase updateCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberByIdUseCase getCastMemberUseCase;

    @MockBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_ShouldReturnUtsIdentifier() throws Exception {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();

        final var expectedId = CastMemberID.from(UUID.randomUUID());
        final var aCommad = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenReturn(CreateCastMemberOutput.from(expectedId));
        //when
        final var aRequest = post("/cast_members")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommad));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());
        //then

        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/cast_members/" + expectedId))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(createCastMemberUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommad = new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
                .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));
        //when
        final var aRequest = post("/cast_members")
                .content(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(aCommad));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());
        //then

        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase).execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));


    }
}
