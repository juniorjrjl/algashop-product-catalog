package com.algaworks.algashop.product.catalog.presentation;

public class UnprocessableContentException extends RuntimeException {

    public UnprocessableContentException() {
    }

    public UnprocessableContentException(final String message) {
        super(message);
    }

    public UnprocessableContentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UnprocessableContentException(final Throwable cause) {
        super(cause);
    }

    public UnprocessableContentException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
