package com.nesrux.admin.catalogo.api;

import com.nesrux.admin.catalogo.ControllerTest;
import com.nesrux.admin.catalogo.infrastructure.api.CategoryAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void test() {
    }
}
