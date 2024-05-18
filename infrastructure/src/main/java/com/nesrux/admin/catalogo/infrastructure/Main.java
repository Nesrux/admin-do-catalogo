package com.nesrux.admin.catalogo.infrastructure;

import com.nesrux.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        // System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME,
        // "development");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "low");
        SpringApplication.run(WebServerConfig.class, args);
    }

}