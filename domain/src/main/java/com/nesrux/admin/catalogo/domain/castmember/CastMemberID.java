package com.nesrux.admin.catalogo.domain.castmember;

import com.nesrux.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public static CastMemberID unique() {
        return CastMemberID.from(UUID.randomUUID());
    }

    public static CastMemberID from(final String AnId) {
        return new CastMemberID((AnId));
    }

    public static CastMemberID from(final UUID anId) {
        return new CastMemberID(anId.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
