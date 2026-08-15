package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainEventPublisher;
import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final QuantityInStockAdjustment quantityInStockAdjustment;
    private final DomainEventPublisher domainEventPublisher;

    public void restock(final UUID productId, final Integer stockAmount) {
        if (stockAmount < 1) {
            throw new IllegalArgumentException();
        }

        final QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.increase(productId, stockAmount);
        } catch (final Exception _) {
            throw new DomainException(String.format("Failed to restock product %s stock", productId));
        }

        if (result.inRestocked()){
            domainEventPublisher.publish(ProductRestockedEvent.builder().id(productId).build());
        }
    }

    public void withdraw(final UUID productId, final Integer stockAmount) {
        if (stockAmount < 1) {
            throw new IllegalArgumentException();
        }

        final QuantityInStockAdjustment.Result result;
        try {
            result = quantityInStockAdjustment.decrease(productId, stockAmount);
        } catch (final Exception _) {
            throw new DomainException(String.format("Failed to withdraw product %s stock", productId));
        }

        if (result.isOutOfStock()){
            domainEventPublisher.publish(ProductSoldOutEvent.builder().id(productId).build());
        }
    }

}
