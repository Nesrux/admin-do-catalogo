package com.nesrux.admin.catalogo.infrastructure.amq;

import com.nesrux.admin.catalogo.application.video.media.update.UpdateMediaStatusCommand;
import com.nesrux.admin.catalogo.application.video.media.update.UpdateMediaStatusUseCase;
import com.nesrux.admin.catalogo.domain.video.MediaStatus;
import com.nesrux.admin.catalogo.infrastructure.configuration.json.Json;
import com.nesrux.admin.catalogo.infrastructure.video.models.rabbitmq.VideoEncoderCompleted;
import com.nesrux.admin.catalogo.infrastructure.video.models.rabbitmq.VideoEncoderError;
import com.nesrux.admin.catalogo.infrastructure.video.models.rabbitmq.VideoEnconderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {

    public static final String LISTENER_ID = "videoEncodedListener";
    private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);

    private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

    public VideoEncoderListener(UpdateMediaStatusUseCase updateMediaStatusUseCase) {
        this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var aResult = Json.readValue(message, VideoEnconderResult.class);

        if (aResult instanceof VideoEncoderCompleted dto) {
            log.error("[message: video.listener.income] [status:completed] [payload:{}]", message);
            final var aCommand = new UpdateMediaStatusCommand(
                    MediaStatus.COMPLETED,
                    dto.id(),
                    dto.video().resourceId(),
                    dto.video().encondedVideoFolder(),
                    dto.video().filePath()
            );
        } else if (aResult instanceof VideoEncoderError dto) {
            log.error("[message: video.listener.income] [status:error] [payload:{}]", message);
        } else {
            log.error("[message: video.listener.income] [status:unknown] [payload:{}]", message);

        }

    }

}
