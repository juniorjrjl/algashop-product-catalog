package com.algaworks.algashop.product.catalog.application.category.management;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryManagementApplicationService {

    private final CategoryRepository repository;

    public UUID create(final CategoryInput input) {
        final var domain = new Category(input.getName(), input.getEnabled());
        repository.save(domain);
        return domain.getId();
    }

    public void update(final UUID id, final CategoryInput input) {
        final var domain = repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        domain.setName(input.getName());
        domain.setEnabled(input.getEnabled());
        repository.save(domain);
    }

    public void disable(final UUID id) {
        final var domain = repository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        domain.setEnabled(false);
        repository.save(domain);
    }
}