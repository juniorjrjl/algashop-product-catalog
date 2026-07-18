package com.algaworks.algashop.product.catalog.infrastructure.persistence.cateogory;

import com.algaworks.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProductCategoryUpdater {

    private final MongoOperations mongoOperations;

    public void copyCategoryDataToProducts(final CategoryUpdatedEvent event){
        final var query = new Query(
                Criteria.where("category._id").is(event.getId())
        );
        final var update = new Update()
                .set("category.name", event.getName())
                .set("category.enabled", event.isEnabled());
        mongoOperations.updateMulti(query, update, Product.class);
    }

}
