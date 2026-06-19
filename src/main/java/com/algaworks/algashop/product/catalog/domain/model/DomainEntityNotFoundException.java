package com.algaworks.algashop.product.catalog.domain.model;

public class DomainEntityNotFoundException extends RuntimeException{

    public DomainEntityNotFoundException() {
    }

    public DomainEntityNotFoundException(final String message) {
        super(message);
    }

    public DomainEntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DomainEntityNotFoundException(final Throwable cause) {
        super(cause);
    }

    public DomainEntityNotFoundException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
