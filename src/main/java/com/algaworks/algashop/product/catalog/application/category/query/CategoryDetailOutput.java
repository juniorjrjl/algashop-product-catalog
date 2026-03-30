package com.algaworks.algashop.product.catalog.application.category.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CategoryDetailOutput {

    private final UUID id;
    private final String name;
    private final Boolean enabled;

    public CategoryDetailOutput(@JsonProperty("id")
                                final UUID id,
                                @JsonProperty("name")
                                final String name,
                                @JsonProperty("enabled")
                                final Boolean enabled) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
    }

}
