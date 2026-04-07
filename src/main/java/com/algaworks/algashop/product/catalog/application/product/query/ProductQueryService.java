package com.algaworks.algashop.product.catalog.application.product.query;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductQueryService {

    public ProductDetailOutput findById(final UUID id){
        return null;
    }

    public PageModel<ProductDetailOutput> filter(final Integer size, final Integer number){
        return null;
    }

}
