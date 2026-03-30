package com.algaworks.algashop.product.catalog.application.category.management;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class CategoryInput {

    private final String name;
    private final Boolean enabled;

    public CategoryInput(@NotBlank
                         @JsonProperty("name")
                         final String name,
                         @NotNull
                         @JsonProperty("enabled")
                         final Boolean enabled) {
        this.name = name;
        this.enabled = enabled;
    }

}
