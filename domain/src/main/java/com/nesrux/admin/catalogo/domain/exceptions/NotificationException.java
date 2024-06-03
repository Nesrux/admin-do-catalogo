package com.nesrux.admin.catalogo.domain.exceptions;

import com.nesrux.admin.catalogo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification notification) {
        super(aMessage, notification.getErrors());
    }

    public String firstErrorMessage() {
        return this.errors.get(0).message();
    }

    public int size() {
        return this.errors.size();
    }

}
