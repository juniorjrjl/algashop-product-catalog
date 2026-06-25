package com.algaworks.algashop.product.catalog.infrastructure.persistence.cateogory;

import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryFilter;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.category.CategoryRepository;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository repository;
    private final CategoryDisassembler disassembler;
    private final MongoOperations mongoOperations;

    @Override
    public PageModel<CategoryDetailOutput> filter(final CategoryFilter filter) {
        final var query = queryWith(filter);
        final var totalItems = mongoOperations.count(query, Product.class);
        final var sort = Sort.by(
                filter.getSortDirectionOrDefault(),
                filter.getSortByPropertOrDefault().getPropertyName()
        );
        final var pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        final var pagedQuery = query.with(pageRequest);
        final List<Category> domain;
        final int totalPages;
        if (totalItems > 0){
            totalPages = (int)Math.ceil((double) totalItems / pageRequest.getPageSize());
            domain = mongoOperations.find(pagedQuery, Category.class);
        } else {
            domain = Collections.emptyList();
            totalPages = 0;
        }
        final var output = disassembler.toDetailOutput(domain);
        return PageModel.<CategoryDetailOutput>builder()
                .content(output)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalElements(totalItems)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public CategoryDetailOutput findById(final UUID categoryId) {
        final var domain = repository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        return disassembler.toDetailOutput(domain);
    }

    private Query queryWith(final CategoryFilter filter) {
        final var query = new Query();
        if (nonNull(filter.getEnabled())) {
            query.addCriteria(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (StringUtils.isNotBlank(filter.getName())) {
            query.addCriteria(Criteria.where("name").regex(filter.getName().trim(), "i"));
        }

        return query;
    }

}
