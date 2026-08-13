package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.QuantityInStockAdjustment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class QuantityInStockAdjustmentMongoImpl implements QuantityInStockAdjustment {

    private final MongoOperations mongoOperations;

    @Override
    public void increase(final UUID productId, final Integer stockAmount) {
        final var query = Query.query(Criteria.where("id").is(productId));
        changeStockQuantity(productId, stockAmount, query);
    }


    @Override
    public void decrease(final UUID productId, final Integer stockAmount) {
        final var query = Query.query(Criteria.where("id")
                .is(productId)
                .and("stockAmount").gte(stockAmount));
        changeStockQuantity(productId, stockAmount * -1, query);
    }

    private void changeStockQuantity(final UUID productId,
                                     final Integer stockAmount,
                                     final Query query) {

        final var update = new Update()
                .inc("stockAmount", stockAmount)
                .inc("version", 1)
                .set("updatedAt", OffsetDateTime.now());
        final var updateResult = mongoOperations.update(Product.class)
                .matching(query)
                .apply(update)
                .first();
        if (updateResult.getModifiedCount() < 1) {
            throw new StockUpdateFailerException(String.format("Product with id %s was not found", productId));
        }
    }

}
