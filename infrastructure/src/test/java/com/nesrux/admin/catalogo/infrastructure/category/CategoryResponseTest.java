package com.nesrux.admin.catalogo.infrastructure.category;

import com.nesrux.admin.catalogo.JacksonTest;
import com.nesrux.admin.catalogo.infrastructure.category.models.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CategoryResponseTest {
    @Autowired
    private JacksonTester<CategoryResponse> json;

}
