package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductFilter;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationExpressionCriteria;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository repository;
    private final ProductDisassembler disassembler;
    private final MongoOperations mongoOperations;

    @Override
    public ProductDetailOutput findById(final UUID id){
        return repository.findById(id)
                .map(disassembler::toDetailOutput)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(final ProductFilter filter){
        final var query = queryWith(filter);
        final var totalItems = mongoOperations.count(query, Product.class);
        final var sort = sortWith(filter);
        final var pageRequest = PageRequest.of(filter.getPage(), filter.getSize(), sort);
        final var pagedQuery = query.with(pageRequest);
        final List<Product> domain;
        final int totalPages;
        if (totalItems > 0){
            totalPages = (int)Math.ceil((double) totalItems / pageRequest.getPageSize());
            domain = mongoOperations.find(pagedQuery, Product.class);
        } else {
            domain = Collections.emptyList();
            totalPages = 0;
        }
        final var output = disassembler.toSummaryOutput(domain);
        return PageModel.<ProductSummaryOutput>builder()
                .content(output)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalElements(totalItems)
                .totalPages(totalPages)
                .build();
    }

    private Sort sortWith(final ProductFilter filter) {
        if (nonNull(filter.getTerm()) && (!filter.getTerm().isBlank())) {
            return Sort.by("score");
        } else {
            return Sort.by(
                    filter.getSortDirectionOrDefault(),
                    filter.getSortByPropertOrDefault().getPropertyName()
            );
        }
    }

    private Query queryWith(final ProductFilter filter) {
        final var query = new Query();
        if (nonNull(filter.getEnabled())) {
            query.addCriteria(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (nonNull(filter.getCreatedAtFrom()) && nonNull(filter.getCreatedAtTo())) {
            query.addCriteria(
                    Criteria.where("createdAt")
                            .gte(filter.getCreatedAtFrom())
                            .lte(filter.getCreatedAtTo())
            );
        } else {
            if (nonNull(filter.getCreatedAtFrom())){
                query.addCriteria(Criteria.where("createdAt").gte(filter.getCreatedAtFrom()));
            }
            if (nonNull(filter.getCreatedAtTo())){
                query.addCriteria(Criteria.where("createdAt").lte(filter.getCreatedAtTo()));
            }
        }
        if (nonNull(filter.getPriceFrom()) && nonNull(filter.getPriceTo())) {
            query.addCriteria(
                    Criteria.where("salePrice")
                            .gte(filter.getPriceFrom())
                            .lte(filter.getPriceTo())
            );
        } else {
            if (nonNull(filter.getPriceFrom())){
                query.addCriteria(Criteria.where("salePrice").gte(filter.getPriceFrom()));
            }
            if (nonNull(filter.getPriceTo())){
                query.addCriteria(Criteria.where("salePrice").lte(filter.getPriceTo()));
            }
        }

        if (nonNull(filter.getHasDiscount())){
            if (filter.getHasDiscount()){
                query.addCriteria(AggregationExpressionCriteria.whereExpr(
                        ComparisonOperators.valueOf("$salePrice")
                                .lessThan("$regularPrice")
                        ));
            } else {
                query.addCriteria(AggregationExpressionCriteria.whereExpr(
                        ComparisonOperators.valueOf("$salePrice")
                                .equalTo("$regularPrice")
                ));
            }
        }

        if (nonNull(filter.getInStock())){
            if (filter.getInStock()){
                query.addCriteria(Criteria.where("stockAmount").gt(0));
            } else {
                query.addCriteria(Criteria.where("stockAmount").is(0));
            }
        }

        if (nonNull(filter.getCategoriesId()) && filter.getCategoriesId().length > 0){
            query.addCriteria(Criteria.where("categoryId").in((Object[]) filter.getCategoriesId()));
        }

        if (nonNull(filter.getTerm()) && (!filter.getTerm().isBlank())){
            query.addCriteria(TextCriteria.forDefaultLanguage().matching(filter.getTerm()));
        }

        return query;
    }

}
