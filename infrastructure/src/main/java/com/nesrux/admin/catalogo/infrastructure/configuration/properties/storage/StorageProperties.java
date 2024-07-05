package com.nesrux.admin.catalogo.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(StorageProperties.class);

    private String locationPattern;
    private String filenamePattern;

    public StorageProperties() {
    }

    public String locationPattern() {
        return locationPattern;
    }

    public StorageProperties setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
        return this;
    }

    public String filenamePattern() {
        return filenamePattern;
    }

    public StorageProperties setFileNamePattern(String fileNamePattern) {
        this.filenamePattern = fileNamePattern;
        return this;
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "locationPattern='" + locationPattern + '\'' +
                ", filenamePattern='" + filenamePattern + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toString());
    }
}
