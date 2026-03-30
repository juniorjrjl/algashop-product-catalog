package com.algaworks.algashop.product.catalog.presentation;

import com.algaworks.algashop.product.catalog.application.category.management.CategoryInput;
import com.algaworks.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
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

@RestController
@RequestMapping("/api/{version}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;
    private final CategoryManagementApplicationService categoryManagementService;

    @GetMapping
    public PageModel<CategoryDetailOutput> filter(@RequestParam(defaultValue = "0") final Integer page,
                                                  @RequestParam(defaultValue = "10") final Integer size) {
        return categoryQueryService.filter(size,page);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public CategoryDetailOutput create(@RequestBody @Valid final CategoryInput input) {
        final var categoryId = categoryManagementService.create(input);
        return categoryQueryService.findById(categoryId);
    }

    @GetMapping("/{id}")
    public CategoryDetailOutput findById(@PathVariable final UUID id) {
        return categoryQueryService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoryDetailOutput update(
            @PathVariable final UUID id,
            @RequestBody @Valid final CategoryInput input) {
        categoryManagementService.update(id, input);
        return categoryQueryService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void disable(@PathVariable final UUID id) {
        categoryManagementService.disable(id);
    }

}