package com.algaworks.algashop.product.catalog.presentation;

import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/{version}/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductQueryService queryService;
    private final ProductManagementApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ProductDetailOutput create(@RequestBody @Valid final ProductInput input){
        final UUID id;
        try{
            id = applicationService.create(input);
        }catch (final CategoryNotFoundException ex){
            throw new UnprocessableContentException(ex.getMessage(), ex);
        }
        return queryService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public ProductDetailOutput update(@PathVariable final UUID id,
                                      @RequestBody @Valid final ProductInput input) {
        applicationService.update(id, input);
        return queryService.findById(id);
    }

    @DeleteMapping("/{id}/enable")
    @ResponseStatus(NO_CONTENT)
    public void disable(@PathVariable final UUID id) {
        applicationService.disable(id);
    }

    @PutMapping("/{id}/enable")
    @ResponseStatus(NO_CONTENT)
    public void enable(@PathVariable final UUID id) {
        applicationService.enable(id);
    }

    @GetMapping("/{id}")
    public ProductDetailOutput findById(@PathVariable final UUID id) {
        return queryService.findById(id);
    }

    @GetMapping
    public PageModel<ProductSummaryOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        return queryService.filter(size, number);
    }

}
