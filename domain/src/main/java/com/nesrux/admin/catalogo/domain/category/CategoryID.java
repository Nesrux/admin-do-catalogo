package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    public static CategoryID unique() {
        return CategoryID.from(UUID.randomUUID());
    }

    public static CategoryID from(final String AnId) {
        return new CategoryID(AnId);
    }

    public static CategoryID from(final UUID anId) {
        return new CategoryID(anId.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

}
