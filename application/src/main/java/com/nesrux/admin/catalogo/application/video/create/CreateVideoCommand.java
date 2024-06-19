package com.nesrux.admin.catalogo.application.video.create;

import com.nesrux.admin.catalogo.domain.video.Resource;

import java.util.Set;

public record CreateVideoCommand(
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource thailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf
) {
}
