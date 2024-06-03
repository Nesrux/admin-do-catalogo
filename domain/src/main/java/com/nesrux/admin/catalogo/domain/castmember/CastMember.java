package com.nesrux.admin.catalogo.domain.castmember;

import com.nesrux.admin.catalogo.domain.AggregateRoot;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.utils.InstantUtils;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    public CastMember(final CastMemberID anId,
                      final String aName,
                      final CastMemberType aType,
                      final Instant aCreationDate,
                      final Instant aUpdateDate) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var castMemberID = CastMemberID.unique();
        final var now = InstantUtils.now();

        return new CastMember(
                castMemberID, aName, aType, now, now);
    }

    public CastMember update(final String aName, final CastMemberType aType){
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()) {
            throw new NotificationException("Failed to create Aggregate CastMember", notification);
        }
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(aHandler, this).validate();
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
