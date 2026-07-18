package com.algaworks.algashop.product.catalog.infrastructure.listener.category;

import com.algaworks.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.cateogory.ProductCategoryUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoryEventListener {

    private final ProductCategoryUpdater productCategoryUpdater;

    @EventListener
    @Async
    public void handle(final CategoryUpdatedEvent event) {
        productCategoryUpdater.copyCategoryDataToProducts(event);
    }

}
