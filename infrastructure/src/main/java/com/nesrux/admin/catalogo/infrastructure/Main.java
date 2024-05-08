package com.nesrux.admin.catalogo.infrastructure;

import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.list.ListCategoriesUSeCase;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.nesrux.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);
    }

    @Bean
    @DependsOnDatabaseInitialization
    ApplicationRunner runner(CreateCategoryUseCase c, UpdateCategoryUseCase p, GetCategoryByIdUseCase ge, ListCategoriesUSeCase li, DeleteCategoryUseCase de) {
        return args -> {

        };
    }

}