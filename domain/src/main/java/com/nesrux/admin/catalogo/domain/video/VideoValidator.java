package com.nesrux.admin.catalogo.domain.video;

import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.Validator;

public class VideoValidator extends Validator {
    private final Video video;

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;

    protected VideoValidator(final Video video, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkTitleConstraint();
        checkDescriptionConstraint();
        checkLaunchConstraint();
        checkRatingConstraint();
    }

    private void checkTitleConstraint() {
        final var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }
        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }
        final int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
        }
    }

    private void checkDescriptionConstraint() {
        final var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }
        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }
        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error("'description' must be between 1 and 4000 characters"));
        }
    }

    private void checkLaunchConstraint() {
        if (this.video.getLaunchedAt() == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraint() {
        if (this.video.getRating() == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
