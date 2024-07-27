package com.nesrux.admin.catalogo.infrastructure.video.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record CreateVideoRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("duration") Double duration,
        @JsonProperty("year_launched") Integer yearLaunched,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres,
        @JsonProperty("cast_members") Set<String> castMembers
) {

    public static CreateVideoRequest with(
            String title,
            String description,
            Double duration,
            Integer yearLaunched,
            Boolean opened,
            Boolean published,
            String rating,
            Set<String> castMembers,
            Set<String> categories,
            Set<String> genres
    ) {
        return new CreateVideoRequest(
                title,
                description,
                duration,
                yearLaunched,
                opened,
                published,
                rating,
                castMembers,
                categories,
                genres
        );
    }
}
