package com.nesrux.admin.catalogo.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.nesrux.admin.catalogo.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.nesrux.admin.catalogo.infrastructure.services.StorageService;
import com.nesrux.admin.catalogo.infrastructure.services.impl.GCStorageService;
import com.nesrux.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean(name = "StorageService")
    @Profile({"low", "development", "production"})
    public StorageService gcStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean(name = "StorageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
