package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class ProductNotFoundException extends DomainEntityNotFoundException {

    public ProductNotFoundException(final UUID id) {
        super(String.format("Product with id %s not found", id));
    }

}
