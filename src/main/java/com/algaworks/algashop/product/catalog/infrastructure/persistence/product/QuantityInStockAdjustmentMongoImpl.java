package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.QuantityInStockAdjustment;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.Objects.isNull;


@Component
@RequiredArgsConstructor
public class QuantityInStockAdjustmentMongoImpl implements QuantityInStockAdjustment {

    private final MongoOperations mongoOperations;

    @Override
    public Result increase(final UUID productId, final Integer stockAmount) {
        final var query = Query.query(Criteria.where("id").is(productId));
        return changeStockQuantity(productId, stockAmount, query);
    }


    @Override
    public Result decrease(final UUID productId, final Integer stockAmount) {
        final var query = Query.query(Criteria.where("id")
                .is(productId)
                .and("stockAmount").gte(stockAmount));
        return changeStockQuantity(productId, stockAmount * -1, query);
    }

    private Result changeStockQuantity(final UUID productId,
                                       final Integer stockAmount,
                                       final Query queryForUpdate) {
        final var findProductAmount = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("id").is(productId)),
                Aggregation.project("stockAmount", "version", "updatedAt")
        );

        final var productBeforeUpdate = mongoOperations.aggregate(
                findProductAmount, Product.class,
                Document.class
        ).getUniqueMappedResult();

        if (isNull(productBeforeUpdate)){
            throw new ProductNotFoundException(productId);
        }

        final var previousStockAmount = productBeforeUpdate.getInteger("stockAmount");

        final var update = new Update()
                .inc("stockAmount", stockAmount)
                .inc("version", 1)
                .set("updatedAt", OffsetDateTime.now());
        final var productUpdated = mongoOperations.findAndModify(
                queryForUpdate,
                update,
                new FindAndModifyOptions().returnNew(true),
                Product.class
        );
        if (isNull(productUpdated)) {
            throw new StockUpdateFailerException(
                    String.format("Failed to update stock amount for product %s", productId)
            );
        }

        final var newStockAmount = productUpdated.getStockAmount();
        return new Result(productId, previousStockAmount, newStockAmount);
    }

}
