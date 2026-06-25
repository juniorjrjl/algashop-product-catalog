package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.application.SortablePageFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ProductFilter extends SortablePageFilter<ProductFilter.SortType> {

    @Nullable
    private final String term;
    @Nullable
    private final Boolean hasDiscount;
    @Nullable
    private final Boolean enabled;
    @Nullable
    private final Boolean inStock;
    @Nullable
    private final BigDecimal priceFrom;
    @Nullable
    private final BigDecimal priceTo;
    @Nullable
    private final UUID[] categoriesId;
    @Nullable
    private final OffsetDateTime createdAtFrom;
    @Nullable
    private final OffsetDateTime createdAtTo;

    public ProductFilter(@Nullable
                         final Integer pageNumber,
                         @Nullable
                         final Integer pageSize,
                         final ProductFilter. @Nullable SortType sortByProperty,
                         final Sort. @Nullable Direction sortDirection,
                         @Nullable
                         final String term,
                         @Nullable
                         final Boolean hasDiscount,
                         @Nullable
                         final Boolean enabled,
                         @Nullable
                         final Boolean inStock,
                         @Nullable
                         final BigDecimal priceFrom,
                         @Nullable
                         final BigDecimal priceTo,
                         @Nullable
                         final UUID[] categoriesId,
                         @Nullable
                         final OffsetDateTime createdAtFrom,
                         @Nullable
                         final OffsetDateTime createdAtTo) {
        super(pageNumber, pageSize, sortByProperty, sortDirection);
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


    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        CREATED_AT("createdAt"),
        SALE_PRICE("salePrice");

        private final  String propertyName;
    }

}
