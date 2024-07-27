package com.nesrux.admin.catalogo.domain.exceptions;

public class NoStacktraceException extends RuntimeException {


    public NoStacktraceException(final String message) {
        this(message, null);
    }

    /* Oque basicamente esse construtor faz é não printar a StackTrace da exception
     * porque, em contexto de microsserviços a JVM gasta muito processamento para printar uma
     * Stacktrace completa, e isso gasta recurso e tempo*/
    public NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
