package com.nesrux.admin.catalogo.infrastructure.configuration.properties.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class QueueProperties implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(QueueProperties.class);
    private String exchange;
    private String routingKey;
    private String queue;

    public QueueProperties() {
    }

    public String exchange() {
        return exchange;
    }

    public String routingKey() {
        return routingKey;
    }

    public String queue() {
        return queue;
    }

    @Override
    public String toString() {
        return "QueueProperties{" +
                "exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", queue='" + queue + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug(toString());
    }
}
