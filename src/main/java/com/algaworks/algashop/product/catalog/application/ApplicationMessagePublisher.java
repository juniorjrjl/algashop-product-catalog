package com.algaworks.algashop.product.catalog.application;

public interface ApplicationMessagePublisher {

    void send(final Object message);

}
