package com.algaworks.algashop.product.catalog.utility.databuilder;

import com.algaworks.algashop.product.catalog.application.product.query.CategoryMinimalOutput;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.UUID;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class CategoryMinimalOutputDataBuilder {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    @With
    private Supplier<UUID> id = UUID::randomUUID;
    @With
    private Supplier<String> name = () -> customFaker.book().title();

    public static CategoryMinimalOutputDataBuilder builder() {
        return new CategoryMinimalOutputDataBuilder();
    }

    public CategoryMinimalOutput build() {
        return CategoryMinimalOutput.builder()
                .id(id.get())
                .name(name.get())
                .build();
    }

}
