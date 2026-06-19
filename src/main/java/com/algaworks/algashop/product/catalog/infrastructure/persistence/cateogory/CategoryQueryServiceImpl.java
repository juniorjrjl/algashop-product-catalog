package com.algaworks.algashop.product.catalog.infrastructure.persistence.cateogory;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository repository;
    private final CategoryDisassembler disassembler;

    @Override
    public PageModel<CategoryDetailOutput> filter(final Integer size, final Integer number) {
        return null;
    }

    @Override
    public CategoryDetailOutput findById(final UUID categoryId) {
        final var domain = repository.findById(categoryId)
                .orElseThrow(ResourceNotFoundException::new);
        return disassembler.toDetailOutput(domain);
    }
}
