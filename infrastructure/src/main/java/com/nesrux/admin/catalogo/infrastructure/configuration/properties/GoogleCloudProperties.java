package com.nesrux.admin.catalogo.infrastructure.configuration.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class GoogleCloudProperties implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudProperties.class);
    private String credentials;

    private String projectId;

    public String getCredentials() {
        return credentials;
    }

    public GoogleCloudProperties setCredentials(String credentials) {
        this.credentials = credentials;
        return this;
    }

    public String getProjectId() {
        return projectId;
    }

    public GoogleCloudProperties setProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    @Override
    public void afterPropertiesSet() {
        LOG.debug(toString());
    }

    @Override
    public String toString() {
        return "GoogleCloudProperties{" +
                "credentials='" + credentials + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}
