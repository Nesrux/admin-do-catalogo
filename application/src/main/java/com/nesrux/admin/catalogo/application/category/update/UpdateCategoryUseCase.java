package com.nesrux.admin.catalogo.application.category.update;

import com.nesrux.admin.catalogo.application.UseCase;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand,
        Either<Notification, UpdateCategoryOutput>> {
}
