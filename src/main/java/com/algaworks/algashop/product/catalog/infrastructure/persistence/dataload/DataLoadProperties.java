package com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties("algashop.data-load")
@Validated
public record DataLoadProperties(
        @NotNull
        Boolean enabled,
        @NotNull
        Boolean autoClean,
        @NotNull
        List<@Valid DataLoadSource> sources
) {

    @Validated
    public record DataLoadSource(
            @NotBlank
            String location,
            @NotBlank
            String collection
    ){}

}
