package com.algaworks.algashop.product.catalog.domain.model.product;

import java.util.UUID;

public interface QuantityInStockAdjustment {

    Result increase(final UUID productId, final Integer stockAmount);

    Result decrease(final UUID productId, final Integer stockAmount);

    record Result(
            UUID productId,
            int previousStockAmount,
            int newStockAmount
    ){

        public boolean isOutOfStock() {
            return newStockAmount == 0 && previousStockAmount != 0;
        }

        public boolean inRestocked(){
            return newStockAmount > 0 && previousStockAmount == 0;
        }

    }

}
