package com.nesrux.admin.catalogo.application;

import com.nesrux.admin.catalogo.IntegrationTest;
import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {
    @Autowired
    private CreateCategoryUseCase createUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void test() {
        Assertions.assertNotNull(createUseCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
