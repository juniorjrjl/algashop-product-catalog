package com.algaworks.algashop.product.catalog.application.product.management;

import com.algaworks.algashop.product.catalog.application.product.ProductAssembler;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductManagementApplicationService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductAssembler assembler;

    public UUID create(final ProductInput input) {
        final var category = findCategoryById(input.getCategoryId());
        final var domainModel = assembler.toDomainModel(input, category);
        repository.save(domainModel);
        return domainModel.getId();
    }

    public void update(final UUID id, final ProductInput input) {
        final var domainModel = findById(id);
        final var category = findCategoryById(input.getCategoryId());
        assembler.updatedDomainModel(input, category, domainModel);
        repository.save(domainModel);
    }

    public void disable(final UUID id) {
        final var domainModel = findById(id);
        domainModel.disable();
        repository.save(domainModel);
    }

    public void enable(final UUID id) {
        final var domainModel = findById(id);
        domainModel.enable();
        repository.save(domainModel);
    }

    private Product findById(final UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private Category findCategoryById(final UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

}
