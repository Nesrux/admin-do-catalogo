package com.nesrux.admin.catalogo.infrastructure.configuration.usecase;

import com.nesrux.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.DefaultGetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.get.GetCategoryByIdUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.list.DefaultListCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.retrive.list.ListCategoriesUseCase;
import com.nesrux.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.nesrux.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway gateway;

    public CategoryUseCaseConfig(CategoryGateway gateway) {
        this.gateway = gateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(gateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(gateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(gateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(gateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUSeCase() {
        return new DefaultListCategoryUseCase(gateway);
    }
}
