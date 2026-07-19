package com.algaworks.algashop.product.catalog.infrastructure.listener.product;

import com.algaworks.algashop.product.catalog.domain.model.product.ProductAddedEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductDelistedEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductListedEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductPlacedOnSaleEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductPriceChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductEventListener {

    @EventListener(ProductPriceChangedEvent.class)
    public void handle(final ProductPriceChangedEvent event) {
        log.info("Product price changed: {}", event);
    }

    @EventListener(ProductPlacedOnSaleEvent.class)
    public void handle(final ProductPlacedOnSaleEvent event) {
        log.info("Product placed on sale: {}", event);
    }

    @EventListener(ProductAddedEvent.class)
    public void handle(ProductAddedEvent event) {
        log.info("ProductAddedEvent {}", event);
    }

    @EventListener(ProductDelistedEvent.class)
    public void handle(final ProductDelistedEvent  event) {
        log.info("ProductDelistedEvent  {}", event);
    }

    @EventListener(ProductListedEvent.class)
    public void handle(final ProductListedEvent event) {
        log.info("ProductListedEvent {}", event);
    }

}
