package com.algaworks.algashop.product.catalog.application.category.management;

import com.algaworks.algashop.product.catalog.application.ApplicationMessagePublisher;
import com.algaworks.algashop.product.catalog.application.category.event.CategoryUpdatedEvent;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryManagementApplicationService {

    private final CategoryRepository repository;
    private final ApplicationMessagePublisher publisher;

    public UUID create(final CategoryInput input) {
        final var domain = new Category(input.getName(), input.getEnabled());
        repository.save(domain);
        return domain.getId();
    }

    public void update(final UUID id, final CategoryInput input) {
        final var domain = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        domain.setName(input.getName());
        domain.setEnabled(input.getEnabled());
        repository.save(domain);
        publisher.send(new CategoryUpdatedEvent(domain.getId(), domain.getName(), domain.isEnabled()));
    }

    public void disable(final UUID id) {
        final var domain = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        domain.setEnabled(false);
        repository.save(domain);
        publisher.send(new CategoryUpdatedEvent(domain.getId(), domain.getName(), domain.isEnabled()));
    }
}