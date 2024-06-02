package com.nesrux.admin.catalogo.infrastructure.genre.models;

import com.nesrux.admin.catalogo.JacksonTest;
import com.nesrux.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

@JacksonTest
public class UpdateGenreRequestTest {
    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    public void testUnMarshall() throws Exception {
        final var expectedName = "Terror";
        final var expectedCategories = "123";
        final var expectedIsActive = true;

        final var json = """
        {
          "name": "%s",
          "categories_id": ["%s"],
          "is_active": %s
        }    
        """.formatted(expectedName, expectedCategories, expectedIsActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategories))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
