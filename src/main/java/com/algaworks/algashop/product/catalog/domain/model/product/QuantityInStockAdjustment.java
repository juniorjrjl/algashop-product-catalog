package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

public interface QuantityInStockAdjustment {

    void increase(final UUID productId, final Integer stockAmount);

    void decrease(final UUID productId, final Integer stockAmount);

}
