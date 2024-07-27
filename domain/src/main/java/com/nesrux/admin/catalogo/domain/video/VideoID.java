package com.nesrux.admin.catalogo.domain.video;

import com.nesrux.admin.catalogo.domain.Identifier;
import com.nesrux.admin.catalogo.domain.utils.IdUtils;

import java.util.Objects;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    public static VideoID unique() {
        return VideoID.from(IdUtils.uuid());
    }

    public static VideoID from(final String AnId) {
        return new VideoID(AnId);
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
        final VideoID other = (VideoID) obj;
        return Objects.equals(value, other.value);
    }
}
