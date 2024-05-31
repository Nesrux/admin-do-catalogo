package com.nesrux.admin.catalogo.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nesrux.admin.catalogo.ControllerTest;
import com.nesrux.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.nesrux.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import com.nesrux.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {
        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper mapper;

        @MockBean
        private CreateGenreUseCase createGenreUseCase;

        @Test
        public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
                // given
                final var expectedName = "Ação";
                final var expectedCategories = List.of("123", "456");
                final var expectedIsActive = true;
                final var expectedId = "123";

                final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

                when(createGenreUseCase.execute(any()))
                                .thenReturn(CreateGenreOutput.from(expectedId));

                // when
                final var aRequest = post("/genres")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(aRequest)
                                .andDo(print());

                // then
                response.andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/genres/" + expectedId))
                                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        }

        @Test
        public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
                // given
                final String expectedName = null;
                final var expectedCategories = List.of("123", "456");
                final var expectedIsActive = true;
                final var expectedErrorMessage = "'name' should not be null";

                final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

                when(createGenreUseCase.execute(any()))
                                .thenThrow(new NotificationException("Error",
                                                Notification.create(new Error(expectedErrorMessage))));

                // when
                final var aRequest = post("/genres")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(this.mapper.writeValueAsString(aCommand));

                final var response = this.mvc.perform(aRequest)
                                .andDo(print());

                // then
                response.andExpect(status().isUnprocessableEntity())
                                .andExpect(header().string("Location", nullValue()))
                                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(jsonPath("$.errors", hasSize(1)))
                                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        }

}