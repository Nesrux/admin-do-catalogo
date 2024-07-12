package com.nesrux.admin.catalogo.infrastructure.configuration;

import com.nesrux.admin.catalogo.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    @ConfigurationProperties("amqp.queues.video-created")
    public QueueProperties queueCreatedVideo() {
        return new QueueProperties();
    }

    @Bean
    @ConfigurationProperties("amqp.queues.video-encoded")
    public QueueProperties queueEncodedVideo() {
        return new QueueProperties();
    }
}
