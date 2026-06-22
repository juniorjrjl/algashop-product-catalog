package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.application.SortablePageFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class ProductFilter extends SortablePageFilter<ProductFilter.SortType> {

    private final String term;
    private final Boolean hasDiscount;
    private final Boolean enabled;
    private final Boolean inStock;
    private final BigDecimal priceFrom;
    private final BigDecimal priceTo;
    private final UUID[] categoriesId;
    private final OffsetDateTime createdAtFrom;
    private final OffsetDateTime createdAtTo;

    public ProductFilter(final int pageNumber,
                         final int pageSize,
                         final String term,
                         final Boolean hasDiscount,
                         final Boolean enabled,
                         final Boolean inStock,
                         final BigDecimal priceFrom,
                         final BigDecimal priceTo,
                         final UUID[] categoriesId,
                         final OffsetDateTime createdAtFrom,
                         final OffsetDateTime createdAtTo) {
        super(pageNumber, pageSize);
        this.term = term;
        this.hasDiscount = hasDiscount;
        this.enabled = enabled;
        this.inStock = inStock;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.categoriesId = categoriesId;
        this.createdAtFrom = createdAtFrom;
        this.createdAtTo = createdAtTo;
    }

    @Override
    public SortType getSortByPropertOrDefault() {
        return SortType.CREATED_AT;
    }

    @Override
    public Sort.Direction getSortDirectionOrDefault() {
        return Sort.Direction.ASC;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        CREATED_AT("createdAt"),
        SALE_PRICE("salePrice");

        private final  String propertyName;
    }
}
