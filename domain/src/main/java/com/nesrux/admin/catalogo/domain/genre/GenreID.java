package com.nesrux.admin.catalogo.domain.genre;

import com.nesrux.admin.catalogo.domain.Identifier;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static GenreID unique() {
        return GenreID.from(IdUtils.uuid());
    }

    public static GenreID from(final String AnId) {
        return new GenreID(AnId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenreID other = (GenreID) obj;
        return Objects.equals(value, other.value);
    }

}
