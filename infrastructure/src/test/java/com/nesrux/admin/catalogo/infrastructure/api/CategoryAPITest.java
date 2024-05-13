package com.nesrux.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nesrux.admin.catalogo.ControllerTest;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.CategoryOutput;
import com.nesrux.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.domain.category.Category;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.DomainException;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import com.nesrux.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Test
    public void givenAValidCommand_WhenCallsCreateCAtegory_ShouldReturnCategoryID() throws Exception {
        //given
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //when
        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput
                        .from("123")));

        // Classe importante
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        //then
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));

    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShoulReturnNotification() throws Exception {
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //when
        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        // Classe importante
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));
        //then
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShoulReturnDomainException() throws Exception {
        //given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        //when
        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        // Classe importante
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aInput));

        //then
        this.mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", Matchers.nullValue()))
                .andExpect(header().string("Content-type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_ShouldReturnCategory() throws Exception {
        //given
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        //when
        final var request = get("/categories/{}", expectedId.getValue());
        final var response = this.mvc.perform(request).andDo(print());


        //then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
                .andExpect(jsonPath("$.name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt().toString())));

        verify(getCategoryByIdUseCase, times(1)).execute(expectedId.getValue());

    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFund() throws Exception {
        //given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryId.from("123");


        //when
        final var request =
                get("/categories/{}", expectedId.getValue());

        final var response =
                this.mvc.perform(request).andDo(print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    }
}
