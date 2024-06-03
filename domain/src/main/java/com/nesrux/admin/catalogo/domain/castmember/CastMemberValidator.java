package com.nesrux.admin.catalogo.domain.castmember;

import com.nesrux.admin.catalogo.domain.validation.Error;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember castMember;

    public CastMemberValidator(final ValidationHandler aHandler, final CastMember castMember) {
        super(aHandler);
        this.castMember = castMember;
    }

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;


    @Override
    public void validate() {
        checkNameConstraint();
    }

    private void checkNameConstraint() {
        final var name = this.castMember.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must between 3 and 255 characters"));
        }
    }
}
