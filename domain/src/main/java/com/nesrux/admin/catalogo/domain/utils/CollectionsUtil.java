package com.nesrux.admin.catalogo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionsUtil {
    private CollectionsUtil() {
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> ids, Function<IN, OUT> mapper) {
        if (ids == null) return null;
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}
