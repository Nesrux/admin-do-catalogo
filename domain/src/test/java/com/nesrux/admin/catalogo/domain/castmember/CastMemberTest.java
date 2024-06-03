package com.nesrux.admin.catalogo.domain.castmember;

import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {
    @Test
    public void givenAvalidParams_whenCallsNewNumber_ThenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAvalidParamsWithTypeDirector_whenCallsNewNumber_ThenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.DIRECTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReciveANotification() {
        final String expectedName = null;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedType = CastMemberType.DIRECTOR;

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReciveANotification() {
        final var expectedName = "   ";
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedType = CastMemberType.DIRECTOR;

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

    }

    @Test
    public void givenAInvalidNameWithLenghtMoreThan255_whenCallsNewMember_shouldReciveANotification() {
        final var expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et +
                dolore magna aliqua. Ac turpis egestas maecenas pharetra convallis posuere morbi. Adipiscing elit duis +
                tristique sollicitudin nibh sit amet commodo nulla. Leo
                """;

        final var expectedErrorMessage = "'name' must between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedType = CastMemberType.DIRECTOR;

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

    }


    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReciveANotification() {
        final var expectedName = "Afonso padilia";
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;
        final CastMemberType expectedType = null;

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());

    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldReciveUpdted() {
        final var expectedName = "vin diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vin gasolina", CastMemberType.DIRECTOR);
        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualMember.getUpdatedAt());
        Assertions.assertNotNull(actualMember.getId());

        actualMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullName_shouldReciveNotifications() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin diesel", expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidEmptyName_shouldReciveNotifications() {
        final String expectedName = "    ";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin diesel", expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidMaxCharacters_shouldReciveNotifications() {
        final var expectedName = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et +
                dolore magna aliqua. Ac turpis egestas maecenas pharetra convallis posuere morbi. Adipiscing elit duis +
                tristique sollicitudin nibh sit amet commodo nulla. Leo
                """;

        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorMessage = "'name' must between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin diesel", expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullType_shouldReciveNotifications() {
        final String expectedName = "Sophie Charllote";
        final CastMemberType expectedType = null;

        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        final var actualMember = CastMember.newMember("vin diesel", CastMemberType.ACTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> actualMember.update(expectedName, expectedType));


        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.firstErrorMessage());
        Assertions.assertEquals(expectedErrorCount, actualException.size());
    }


}
