package com.algaworks.algashop.product.catalog.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/{version}/products")
public class ProductController {

    @PostMapping
    @ResponseStatus(CREATED)
    public ProductDetailOutput create(@RequestBody @Valid final ProductInput input){
        return ProductDetailOutput.builder()
                .id(UUID.randomUUID())
                .addedAt(OffsetDateTime.now())
                .name(input.getName())
                .brand(input.getBrand())
                .regularPrice(input.getRegularPrice())
                .salePrice(input.getSalePrice())
                .inStock(false)
                .enabled(input.getEnabled())
                .category(CategoryMinimalOutput.builder()
                        .id(input.getCategoryId())
                        .name("Notebook")
                        .build())
                .description(input.getDescription())
                .build();
    }

    @GetMapping("/{id}")
    public ProductDetailOutput findById(@PathVariable final UUID id) {
        return ProductDetailOutput.builder()
                .id(id)
                .addedAt(OffsetDateTime.now())
                .name("Notebook X11")
                .brand("Deep Driver")
                .regularPrice(new BigDecimal(1500))
                .salePrice(new BigDecimal(1000))
                .inStock(true)
                .enabled(true)
                .category(CategoryMinimalOutput.builder()
                        .id(UUID.randomUUID())
                        .name("Notebook")
                        .build())
                .description("A gamer Notebook")
                .build();
    }

}
