package com.nesrux.admin.catalogo.infrastructure.services;

import com.nesrux.admin.catalogo.domain.event.DomainEvent;

public interface EventService {
    void send(Object event);
}
