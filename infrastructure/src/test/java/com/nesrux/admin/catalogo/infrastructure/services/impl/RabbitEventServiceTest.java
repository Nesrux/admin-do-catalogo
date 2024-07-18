package com.nesrux.admin.catalogo.infrastructure.services.impl;

import com.nesrux.admin.catalogo.domain.video.VideoMediaCreated;
import com.nesrux.admin.catalogo.AmqpTest;
import com.nesrux.admin.catalogo.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.nesrux.admin.catalogo.infrastructure.configuration.json.Json;
import com.nesrux.admin.catalogo.infrastructure.services.EventService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@AmqpTest
public class RabbitEventServiceTest {
    private static final String LISTENER = "video_created";
    @Autowired
    @VideoCreatedQueue
    private EventService eventService;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Test
    public void shouldSendMessage() throws InterruptedException {
        //given
        final var notification = new VideoMediaCreated("resource", "filePath");
        final var expectedMessage = Json.writeValueAsString(notification);

        //when
        this.eventService.send(notification);

        //then
        final var invocationData = harness.getNextInvocationDataFor(LISTENER, 5, TimeUnit.SECONDS);

        Assertions.assertNotNull(invocationData);
        Assertions.assertNotNull(invocationData.getArguments());
        final var actualMessage = (String) invocationData.getArguments()[0];
        Assertions.assertEquals(expectedMessage, actualMessage);

    }

    @Component
    static class VideoCreatedNewsListener {

        @RabbitListener(id = LISTENER,  queues = "${amqp.queues.video-created.routing-key}")
        void onVideoCreated(@Payload String message) {
            System.out.println(message);
        }
    }
}
