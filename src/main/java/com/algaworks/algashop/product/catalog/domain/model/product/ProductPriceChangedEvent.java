package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@ToString
@Builder
@Getter
public class ProductPriceChangedEvent {

    private UUID id;
    @Nullable
    private BigDecimal oldRegularPrice;
    @Nullable
    private BigDecimal oldSalePrice;
    private BigDecimal newRegularPrice;
    private BigDecimal newSalePrice;
    @Builder.Default
    private OffsetDateTime changedAt = OffsetDateTime.now(UTC);

}
