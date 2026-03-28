package com.algaworks.algashop.product.catalog.application.product.query;

import java.util.UUID;

public interface ProductQueryService {

    ProductDetailOutput findById(final UUID id);

    PageModel<ProductDetailOutput> filter(final Integer size, final Integer number);

}
