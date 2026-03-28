package com.algaworks.algashop.product.catalog.presentation;

import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/{version}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService queryService;
    private final ProductManagementApplicationService applicationService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public ProductDetailOutput create(@RequestBody @Valid final ProductInput input){
        final var id = applicationService.create(input);
        return queryService.findById(id);
    }

    @GetMapping("/{id}")
    public ProductDetailOutput findById(@PathVariable final UUID id) {
        return queryService.findById(id);
    }

    @GetMapping
    public PageModel<ProductDetailOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        return queryService.filter(size, number);
    }

}
