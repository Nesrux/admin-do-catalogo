package com.nesrux.admin.catalogo.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void test(){
        var category = new Category();

        Assertions.assertNotNull(category);
    }
}
