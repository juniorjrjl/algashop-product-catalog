package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Document(collection = "products")
@Getter
@NoArgsConstructor(access = PROTECTED)
@CompoundIndex(name = "pidx_product_by_category_enabledTrue_salePrice",
        def = "{'category.Id': 1, 'salePrice': 1}",
        partialFilter = "{'enabled': true}")
@CompoundIndex(name = "pidx_product_by_category_enabledTrue_createdAt",
        def = "{'categoryId': 1, 'createdAt': -1}",
        partialFilter = "{'enabled': true}"
)
public class Product {

    @Setter(PRIVATE)
    private UUID id;
    @TextIndexed(weight = 1)
    private String name;
    @Indexed(name = "idx_product_brand")
    private String brand;
    @TextIndexed(weight = 5)
    @Setter
    @Nullable
    private String description;
    private Integer stockAmount = 0;
    @Setter
    private boolean enabled;
    @Nullable
    private BigDecimal regularPrice;
    @Nullable
    private BigDecimal salePrice;
    @Nullable
    @CreatedDate
    private OffsetDateTime createdAt;
    @Nullable
    @CreatedBy
    private UUID createdByUserId;
    @Nullable
    @LastModifiedDate
    private OffsetDateTime updatedAt;
    @Nullable
    @LastModifiedBy
    private UUID lastModifiedByUserId;
    @Nullable
    @Version
    private Long version;
    @Setter
    private ProductCategory category;
    @Nullable
    private Integer discountPercentageRounded;
    @Nullable
    @TextScore
    private Float score;

    @Builder
    public Product(final String name,
                   final String brand,
                   final String description,
                   final boolean enabled,
                   final BigDecimal regularPrice,
                   final BigDecimal salePrice,
                   final Category category) {
        this.setId(IdGenerator.generateTimeBasedUUID());
        this.setName(name);
        this.setBrand(brand);
        this.setDescription(description);
        this.setEnabled(enabled);
        this.setRegularPrice(regularPrice);
        this.setSalePrice(salePrice);
        this.setCategory(ProductCategory.of(category));
    }

    public void setName(final String name) {
        if (name.isBlank()){
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public void setBrand(final String brand) {
        if (brand.isBlank()){
            throw new IllegalArgumentException();
        }
        this.brand = brand;
    }

    public void setRegularPrice(final BigDecimal regularPrice) {
        if (regularPrice.signum() == -1) {
            throw new IllegalArgumentException();
        }

        if (isNull(salePrice)){
            this.salePrice = regularPrice;
        } else if (regularPrice.compareTo(this.salePrice) < 0){
            throw new DomainException("The sale price cannot be higher than the regular price");
        }
        this.regularPrice = regularPrice;
        this.calculateDiscountPercentage();
    }

    public  void setSalePrice(final BigDecimal salePrice) {
        if (salePrice.signum() == -1) {
            throw new IllegalArgumentException();
        }

        if (isNull(regularPrice)){
            this.regularPrice = salePrice;
        } else if (regularPrice.compareTo(this.salePrice) < 0){
            throw new DomainException("The sale price cannot be higher than the regular price");
        }
        this.salePrice = salePrice;
        this.calculateDiscountPercentage();
    }

    public void enable() {
        setEnabled(true);
    }

    public void disable() {
        setEnabled(false);
    }

    public boolean isInStock() {
        return stockAmount > 0;
    }

    public boolean getHasDiscount(){
        return nonNull(getDiscountPercentageRounded()) && getDiscountPercentageRounded() > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Product product)) return false;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    private void setStockAmount(final Integer stockAmount) {
        if (stockAmount < 0) {
            throw new IllegalArgumentException();
        }
        this.stockAmount = stockAmount;
    }

    private void calculateDiscountPercentage(){
        if (isNull(regularPrice) || isNull(salePrice) || regularPrice.signum() == 0) {
            discountPercentageRounded = 0;
            return;
        }
        discountPercentageRounded = BigDecimal.ONE
                .subtract(salePrice.divide(regularPrice, 4, HALF_UP))
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, HALF_UP)
                .intValue();
    }

}
