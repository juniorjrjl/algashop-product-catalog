package com.algaworks.algashop.product.catalog.utility.databuilder;

import com.algaworks.algashop.product.catalog.application.product.query.CategoryMinimalOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class ProductSummaryOutputDataBuilder {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    @With
    private Supplier<UUID> id = UUID::randomUUID;
    @With
    private Supplier<OffsetDateTime> addedAt = OffsetDateTime::now;
    @With
    private Supplier<String> name = () -> customFaker.boardgame().name();
    @With
    private Supplier<String> brand = () -> customFaker.boardgame().name();
    @With
    private Supplier<BigDecimal> regularPrice = () -> customFaker.numeric().valueBetween(10, 50);
    @With
    private Supplier<BigDecimal> salePrice = () -> customFaker.numeric().valueBetween(70, 120);
    @With
    private Supplier<Boolean> inStock = () -> customFaker.bool().bool();
    @With
    private Supplier<Boolean> enabled = () -> customFaker.bool().bool();
    @With
    private Supplier<CategoryMinimalOutput> category = () -> CategoryMinimalOutputDataBuilder.builder().build();
    @With
    private Supplier<String> description = () -> customFaker.lorem().characters(15);

    public static ProductSummaryOutputDataBuilder builder() {
        return new ProductSummaryOutputDataBuilder();
    }

    public ProductSummaryOutput build() {
        return ProductSummaryOutput.builder()
                .id(id.get())
                .createdAt(addedAt.get())
                .name(name.get())
                .brand(brand.get())
                .regularPrice(regularPrice.get())
                .salePrice(salePrice.get())
                .inStock(inStock.get())
                .enabled(enabled.get())
                .category(category.get())
                .shortDescription(description.get())
                .build();
    }

    public List<ProductSummaryOutput> buildList(final int amount){
        return Stream.generate(this::build)
                .limit(amount).toList();
    }

}
