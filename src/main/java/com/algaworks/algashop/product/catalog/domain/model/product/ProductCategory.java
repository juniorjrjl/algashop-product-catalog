package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ProductCategory {

    private UUID id;
    private String name;
    private boolean enabled;

    public static ProductCategory of(final Category category) {
        return new ProductCategory(category.getId(), category.getName(), category.isEnabled());
    }

}
