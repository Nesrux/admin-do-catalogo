package com.nesrux.admin.catalogo.infrastructure.castmember.models;

import com.nesrux.admin.catalogo.JacksonTest;
import com.nesrux.admin.catalogo.domain.Fixture;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class CastMemberResponseTest {
    @Autowired
    private JacksonTester<CastMemberResponse> json;


    @Test
    public void testMarshall() throws Exception {
        final var expectedId = CastMemberID.unique();
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var response = new CastMemberResponse(
                expectedId.getValue(),
                expectedName,
                expectedType,
                expectedCreatedAt,
                expectedUpdatedAt
        );
        final var actualJson = this.json.write(response);
        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());

    }

}
