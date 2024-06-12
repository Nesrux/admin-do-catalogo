package com.nesrux.admin.catalogo.application.castmember.create;

import com.nesrux.admin.catalogo.domain.castmember.CastMember;
import com.nesrux.admin.catalogo.domain.castmember.CastMemberGateway;
import com.nesrux.admin.catalogo.domain.exceptions.NotificationException;
import com.nesrux.admin.catalogo.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway gateway;

    public DefaultCreateCastMemberUseCase(CastMemberGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }


    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var atype = aCommand.type();

        final var notification = Notification.create();
        final var aMember = notification.validate(() -> CastMember.newMember(aName, atype));

        if (notification.hasError()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.gateway.create(aMember));
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Aggrefate CastMember", notification);
    }
}
