package com.algaworks.algashop.product.catalog.domain.model;

public interface DomainEventPublisher {

    void publish(final Object event);

}
