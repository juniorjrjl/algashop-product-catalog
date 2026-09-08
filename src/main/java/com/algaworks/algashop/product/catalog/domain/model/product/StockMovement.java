package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Document(collation = "stock_movements")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StockMovement {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;
    private OffsetDateTime occuredAt;
    private UUID productId;
    private Integer movementAmount;
    private Integer previousAmount;
    private Integer newAmount;
    private MovementType movementType;

    @Builder
    public StockMovement(final UUID productId,
                         final Integer movementAmount,
                         final Integer previousAmount,
                         final Integer newAmount,
                         final MovementType movementType) {
        this.id = IdGenerator.generateTimeBasedUUID();
        this.occuredAt = OffsetDateTime.now();
        this.productId = productId;
        this.movementAmount = movementAmount;
        this.previousAmount = previousAmount;
        this.newAmount = newAmount;
        this.movementType = movementType;
    }

    public enum  MovementType {
        STOCK_IN, STOCK_OUT;
    }

}
