package com.nesrux.admin.catalogo.e2e.category;

import com.nesrux.admin.catalogo.E2ETest;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.nesrux.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.nesrux.admin.catalogo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@E2ETest
public class CategoryE2ETest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Container is running on port %s\n", mappedPort);
        registry.add("mysql_port", () -> mappedPort);

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Filme";
        final var expectedDescription = "A mais assitida";
        final var expectedIsActive = true;

        final var actualId = givenAcategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retriveACategory(actualId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());

    }

    private CategoryResponse retriveACategory(final String anId) throws Exception {
        final var aRequest = get("/categories/" + anId)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        return Json.readValue(json, CategoryResponse.class);
    }

    private CategoryId givenAcategory(final String name, final String description, boolean isActive) throws Exception {
        final var request = new CreateCategoryRequest(name, description, isActive);

        final var aRequest = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(request));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        // <-- Só uma outra forma de fazer a mesm coisa -->
//        final var actualJson = this.mvc.perform(aRequest)
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse().getContentAsString();
//        final var response = Json.readValue(actualJson, Map.class);
//        return CategoryId.from((String) response.get("id"));
//

        return CategoryId.from(actualId);
    }
}
