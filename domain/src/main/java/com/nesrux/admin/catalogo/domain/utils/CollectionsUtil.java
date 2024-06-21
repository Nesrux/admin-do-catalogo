package com.nesrux.admin.catalogo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionsUtil {
    private CollectionsUtil() {
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> ids, Function<IN, OUT> mapper) {
        return ids.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}
