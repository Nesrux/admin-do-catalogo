package com.nesrux.admin.catalogo.infrastructure.category;

import com.nesrux.admin.catalogo.infrastructure.MySqlGatewayTest;
import com.nesrux.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wildfly.common.Assert;

@MySqlGatewayTest
public class CategoryMySqlGatewayTest {
    @Autowired
    private CategoryMySQLGateway gateway;

    @Autowired
    private CategoryRepository repository;

    @Test
    public void testInjectDependencies() {
        Assert.assertNotNull(gateway);
        Assert.assertNotNull(repository);

    }

}
