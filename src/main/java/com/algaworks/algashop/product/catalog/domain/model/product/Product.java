package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.DomainException;
import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PRIVATE;

@Document(collection = "products")
@Getter
public class Product {

    @Setter(PRIVATE)
    private UUID id;
    private String name;
    private String brand;
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
    @DocumentReference
    @Field(name = "category_id")
    @Setter
    private Category category;
    @Nullable
    private Integer discountPercentageRounded;

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
        this.setCategory(category);
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
