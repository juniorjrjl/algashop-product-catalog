package com.algaworks.algashop.product.catalog.utility.databuilder;

import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ProductInputDataBuilder {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    @With
    private Supplier<String> name = () -> customFaker.boardgame().name();
    @With
    private Supplier<String> brand = () -> customFaker.boardgame().name();
    @With
    private Supplier<BigDecimal> regularPrice = () -> customFaker.numeric().valueBetween(10, 50);
    @With
    private Supplier<BigDecimal> salePrice = () -> customFaker.numeric().valueBetween(70, 120);
    @With
    private Supplier<Boolean> enabled = () -> customFaker.bool().bool();
    @With
    private Supplier<UUID> categoryId = UUID::randomUUID;
    @With
    private Supplier<String> description = () -> customFaker.boardgame().name();

    public static ProductInputDataBuilder builder() {
        return new ProductInputDataBuilder();
    }

    public ProductInput build() {
        return ProductInput.builder()
                .name(name.get())
                .brand(brand.get())
                .regularPrice(regularPrice.get())
                .salePrice(salePrice.get())
                .enabled(enabled.get())
                .categoryId(categoryId.get())
                .description(description.get())
                .build();
    }

}
