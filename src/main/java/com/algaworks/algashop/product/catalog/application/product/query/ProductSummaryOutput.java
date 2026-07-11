package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.utility.Slugfier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductSummaryOutput {

    private UUID id;
    private OffsetDateTime createdAt;
    private String name;
    private String brand;
    private BigDecimal regularPrice;
    private BigDecimal salePrice;
    private Boolean inStock;
    private Boolean enabled;
    private CategoryMinimalOutput category;
    private String shortDescription;
    private Boolean hasDiscount;
    private Integer quantityInStock;
    private Integer discountPercentageRounded;

    @Nullable
    public String getSlug(){
        return Slugfier.slugify(this.name);
    }

}
