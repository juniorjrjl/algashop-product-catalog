package com.algaworks.algashop.product.catalog.utility.databuilder;

import com.algaworks.algashop.product.catalog.application.category.management.CategoryInput;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class CategoryInputDataBuilder {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    @With
    private Supplier<String> name = () -> customFaker.book().title();
    @With
    private Supplier<Boolean> enabled = () -> customFaker.bool().bool();

    public static CategoryInputDataBuilder builder() {
        return new CategoryInputDataBuilder();
    }

    public CategoryInput build() {
        return CategoryInput.builder()
                .name(name.get())
                .enabled(enabled.get())
                .build();
    }

}
