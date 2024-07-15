package com.nesrux.admin.catalogo.domain;

import com.nesrux.admin.catalogo.domain.event.DomainEvent;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class EntityTest {
    @Test
    public void givenNullEvent_whenInstantiete_shouldBeOk() {
        //given
        final var expectedDomainId = new DummyId();
        final List<DomainEvent> expectedDomainEvents = null;

        //when
        final var anEntity = new DummyEntity(expectedDomainId, expectedDomainEvents);

        //then
        Assertions.assertNotNull(anEntity);
        Assertions.assertNotNull(anEntity.getId());
        Assertions.assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_ShouldBeCreated() {
        //given
        final var expectedDomainId = new DummyId();
        final List<DomainEvent> expectedDomainEvents = new ArrayList<>();
        expectedDomainEvents.add(new DummyEvent());
        //when
        final var anEntity = new DummyEntity(expectedDomainId, expectedDomainEvents);

        //then
        Assertions.assertNotNull(anEntity);
        Assertions.assertNotNull(anEntity.getId());
        Assertions.assertEquals(1, anEntity.getDomainEvents().size());

        Assertions.assertThrows(RuntimeException.class, () -> {
            final var actualEvent = anEntity.getDomainEvents();
            actualEvent.add(new DummyEvent());
        });
    }

    @Test
    public void givenEmptyDomainEvents_whenCallsRegisterEvent_ShouldIdEventToList() {
        //given
        final var anEntity = new DummyEntity(new DummyId(), new ArrayList<>());
        final var expectedEvents = 1;
        //when
        anEntity.registerEvent(new DummyEvent());

        //then
        Assertions.assertNotNull(anEntity);
        Assertions.assertNotNull(anEntity.getId());
        Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());

    }

    @Test
    public void givenDomainEvents_whenCallsPublishEvent_ShouldCallPublisherAndCelarTheList() {
        //given
        final var anEntity = new DummyEntity(new DummyId(), new ArrayList<>());
        final var expectedEvents = 0;
        final var expectedSendEvents = 2;
        final var counter = new AtomicInteger(0);

        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());

        Assertions.assertEquals(2, anEntity.getDomainEvents().size());
        //when
        anEntity.publishDomainEvent(event -> {
            counter.incrementAndGet();
        });

        //then
        Assertions.assertNotNull(anEntity);
        Assertions.assertNotNull(anEntity.getId());
        Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        Assertions.assertEquals(expectedSendEvents, counter.get());
    }

    static class DummyEvent implements DomainEvent {

        @Override
        public Instant occurrentOn() {
            return Instant.now();
        }
    }

    /*Classe Dummy para fazer testes de uma classe abstrata*/
    static class DummyId extends Identifier {

        private final String id;

        public DummyId() {
            this.id = IdUtils.uuid();
        }

        @Override
        public String getValue() {
            return this.id;
        }
    }

    static class DummyEntity extends Entity<DummyId> {
        public DummyEntity(DummyId dummyId, List<DomainEvent> domainEvents) {
            super(dummyId, domainEvents);
        }

        @Override
        public void validate(ValidationHandler handler) {

        }
    }
}
