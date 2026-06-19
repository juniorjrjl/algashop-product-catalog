package com.algaworks.algashop.product.catalog.domain.model.category;

import com.algaworks.algashop.product.catalog.domain.model.DomainEntityNotFoundException;

import java.util.UUID;

public class CategoryNotFoundException extends DomainEntityNotFoundException {

    public CategoryNotFoundException(final UUID id) {
        super(String.format("Category with id %s not found", id));
    }

}
