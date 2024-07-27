package com.nesrux.admin.catalogo.domain;

import com.nesrux.admin.catalogo.domain.event.DomainEvent;

import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    public AggregateRoot(final ID id, final List<DomainEvent> events) {
        super(id, events);
    }

    public AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }
}
