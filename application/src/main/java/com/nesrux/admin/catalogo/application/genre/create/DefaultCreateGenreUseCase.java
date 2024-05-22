package com.nesrux.admin.catalogo.application.genre.create;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.nesrux.admin.catalogo.domain.category.CategoryGateway;
import com.nesrux.admin.catalogo.domain.category.CategoryId;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.genre.Genre;
import com.nesrux.admin.catalogo.domain.genre.GenreGateway;
import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultCreateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasError()) {
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryId> ids) {
        final var notification = Notification.create();
        if (ids.isEmpty() || ids == null) {
            return notification;
        }

        final var retrivedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrivedIds.size()) {
            final var commandsIds = new ArrayList<>(ids);
            commandsIds.removeAll(retrivedIds);

            final var missingIdsMessage = commandsIds.stream()
                    .map(CategoryId::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(
                    new Error("Some categories could not be found %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private List<CategoryId> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryId::from)
                .toList();
    }

}