package com.algaworks.algashop.product.catalog.utility.databuilder;

import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class CategoryDetailOutputDataBuilder {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    @With
    private Supplier<UUID> id = UUID::randomUUID;
    @With
    private Supplier<String> name = () -> customFaker.book().title();
    @With
    private Supplier<Boolean> enabled = () -> customFaker.bool().bool();

    public static CategoryDetailOutputDataBuilder builder() {
        return new CategoryDetailOutputDataBuilder();
    }

    public CategoryDetailOutput build() {
        return CategoryDetailOutput.builder()
                .id(id.get())
                .name(name.get())
                .enabled(enabled.get())
                .build();
    }

    public List<CategoryDetailOutput> buildList(final int amount){
        return Stream.generate(this::build)
                .limit(amount).toList();
    }

}
