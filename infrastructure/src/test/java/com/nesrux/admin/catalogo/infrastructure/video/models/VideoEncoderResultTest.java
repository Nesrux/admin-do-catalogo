package com.nesrux.admin.catalogo.infrastructure.video.models;

import com.nesrux.admin.catalogo.JacksonTest;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;
import com.nesrux.admin.catalogo.infrastructure.video.models.rabbitmq.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class VideoEncoderResultTest {
    @Autowired
    private JacksonTester<VideoEnconderResult> json;

    @Test
    public void testUnmarshallSuccessResult() throws Exception {
        //given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "nesruxTest";
        final var expectedStatus = "COMPLETED";
        final var expectedEnconderVideoFolder = "anyFolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetaData = new VideoMetadata(expectedEnconderVideoFolder,
                expectedResourceId, expectedFilePath);


        final var json = """
                    {
                      "status": "%s",
                      "id": "%s",
                      "output_bucket_path": "%s",
                      "video": {
                        "encoded_video_folder": "%s",
                        "resource_id": "%s",
                        "file_path": "%s"
                      }
                    }
                """.formatted(expectedStatus, expectedId, expectedOutputBucket,
                expectedEnconderVideoFolder, expectedResourceId, expectedFilePath);
        //when
        final var actualResult = this.json.parse(json);

        Assertions.assertThat(actualResult)
                .isInstanceOf(VideoEncoderCompleted.class)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("outputBucket", expectedOutputBucket)
                .hasFieldOrPropertyWithValue("status", expectedStatus)
                .hasFieldOrPropertyWithValue("video", expectedMetaData);
    }

    @Test
    public void testMarshallSuccessResult() throws IOException {
        // given
        final var expectedId = IdUtils.uuid();
        final var expectedOutputBucket = "codeeducationtest";
        final var expectedStatus = "COMPLETED";
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata =
                new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);
       /// VideoEncoderCompleted
        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        // when
        final var actualResult = this.json.write(aResult);

        Assertions.assertThat(actualResult)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.output_bucket_path", expectedOutputBucket)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.video.encoded_video_folder", expectedEncoderVideoFolder)
                .hasJsonPathValue("$.video.resource_id", expectedResourceId)
                .hasJsonPathValue("$.video.file_path", expectedFilePath);
    }



    @Test
    public void testUnmarshallErrorResult() throws Exception {
        // given
        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage =
                new VideoMessage(expectedResourceId, expectedFilePath);

        final var json = """
                    {
                      "status": "%s",
                      "error": "%s",
                      "message": {
                        "resource_id": "%s",
                        "file_path": "%s"
                      }
                    }
                """.formatted(expectedStatus, expectedMessage, expectedResourceId, expectedFilePath);

        // when
        final var actualResult = this.json.parse(json);

        Assertions.assertThat(actualResult)
                .isInstanceOf(VideoEncoderError.class) //VideoEncoderError
                .hasFieldOrPropertyWithValue("error", expectedMessage)
                .hasFieldOrPropertyWithValue("message", expectedVideoMessage);
    }

    @Test
    public void testMarshallErrorResult() throws IOException {
        // given
        final var expectedMessage = "Resource not found";
        final var expectedStatus = "ERROR";
        final var expectedResourceId = IdUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedVideoMessage =
                new VideoMessage(expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderError(expectedVideoMessage, expectedMessage);

        // when
        final var actualResult = this.json.write(aResult);

        Assertions.assertThat(actualResult)
                .hasJsonPathValue("$.status", expectedStatus)
                .hasJsonPathValue("$.error", expectedMessage)
                .hasJsonPathValue("$.message.resource_id", expectedResourceId)
                .hasJsonPathValue("$.message.file_path", expectedFilePath);
    }

}
