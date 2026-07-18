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
import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpressionCriteria;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.StringOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        final var criteria = buildCriteria(filter);
        final var textCriteria = buildTextCriteria(filter);
        final var criteriaDefinition = buildCriteriaDefinition(filter);

        final var query = new Query();
        textCriteria.ifPresent(query::addCriteria);
        criteriaDefinition.ifPresent(query::addCriteria);
        criteria.ifPresent(query::addCriteria);

        final var totalElements = mongoOperations.count(query, Product.class);
        if (totalElements == 0){
            return PageModel.<ProductSummaryOutput>builder()
                    .number(0)
                    .size(0)
                    .totalPages(0)
                    .totalElements(0)
                    .content(Collections.emptyList())
                    .build();
        }

        final List<AggregationOperation> operations = new ArrayList<>();

        textCriteria.ifPresent(c -> {
            operations.add(Aggregation.match(c));
            final AggregationOperation addTextScoreField = _ ->
                    new Document("$addFields", new Document("score", new Document("$meta", "textScore")));
            operations.add(addTextScoreField);

        });
        criteria.ifPresent(c -> operations.add(Aggregation.match(c)));
        final var pageRequest = PageRequest.of(filter.getPage(), filter.getSize());

        operations.addAll(Arrays.asList(
                Aggregation.sort(sortWith(filter)),
                projectionForSummary(),
                Aggregation.skip(pageRequest.getOffset()),
                Aggregation.limit(filter.getSize())
        ));
        final var aggregation = Aggregation.newAggregation(operations);
        final var content =  mongoOperations.aggregate(aggregation, Product.class, ProductSummaryOutput.class)
                .getMappedResults();

        final var totalPages = (int) Math.ceil((double) totalElements / (double) pageRequest.getPageSize());

        return PageModel.<ProductSummaryOutput>builder()
                .content(content)
                .number(pageRequest.getPageNumber())
                .size(pageRequest.getPageSize())
                .totalPages(totalPages)
                .totalElements(totalElements)
                .build();
    }

    private ProjectionOperation projectionForSummary(){
        return Aggregation.project()
                .and("_id").as("_id")
                .and("createdAt").as("createdAt")
                .and("name").as("name")
                .and("brand").as("brand")
                .and("regularPrice").as("regularPrice")
                .and("salePrice").as("salePrice")
                .and("enabled").as("enabled")
                .and("stockAmount").as("stockAmount")
                .and("discountPercentageRounded").as("discountPercentageRounded")
                .and("category._id").as("category._id")
                .and("category.name").as("category.name")
                .and("category.enabled").as("category.enabled")
                .and("score").as("score")
                .andExpression("salePrice < regularPrice").as("hasDiscount")
                .andExpression("stockAmount > 0").as("inStock")
                .and(StringOperators.Substr.valueOf("description").substring(0, 50)).as("shortDescription")
                ;
    }

    private Optional<CriteriaDefinition> buildCriteria(final ProductFilter filter) {
        final List<CriteriaDefinition> criteria = new ArrayList<>();
        if (nonNull(filter.getEnabled())) {
            criteria.add(Criteria.where("enabled").is(filter.getEnabled()));
        }

        if (nonNull(filter.getCreatedAtFrom()) && nonNull(filter.getCreatedAtTo())) {
            criteria.add(
                    Criteria.where("createdAt")
                            .gte(filter.getCreatedAtFrom())
                            .lte(filter.getCreatedAtTo())
            );
        } else {
            if (nonNull(filter.getCreatedAtFrom())){
                criteria.add(Criteria.where("createdAt").gte(filter.getCreatedAtFrom()));
            }
            if (nonNull(filter.getCreatedAtTo())){
                criteria.add(Criteria.where("createdAt").lte(filter.getCreatedAtTo()));
            }
        }
        if (nonNull(filter.getPriceFrom()) && nonNull(filter.getPriceTo())) {
            criteria.add(
                    Criteria.where("salePrice")
                            .gte(filter.getPriceFrom())
                            .lte(filter.getPriceTo())
            );
        } else {
            if (nonNull(filter.getPriceFrom())){
                criteria.add(Criteria.where("salePrice").gte(filter.getPriceFrom()));
            }
            if (nonNull(filter.getPriceTo())){
                criteria.add(Criteria.where("salePrice").lte(filter.getPriceTo()));
            }
        }

        if (nonNull(filter.getInStock())){
            if (filter.getInStock()){
                criteria.add(Criteria.where("stockAmount").gt(0));
            } else {
                criteria.add(Criteria.where("stockAmount").is(0));
            }
        }

        if (nonNull(filter.getCategoriesId()) && filter.getCategoriesId().length > 0){
            criteria.add(Criteria.where("category.id").in((Object[]) filter.getCategoriesId()));
        }


        return criteria.isEmpty() ?
                Optional.empty() :
                Optional.of(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
    }

    public Optional<CriteriaDefinition> buildCriteriaDefinition(final  ProductFilter filter) {
        return Optional.ofNullable(filter.getHasDiscount())
                .map(hasDiscount ->{
                    if (hasDiscount){
                        return AggregationExpressionCriteria.whereExpr(
                                ComparisonOperators.valueOf("$salePrice")
                                        .lessThan("$regularPrice")
                        );
                    } else {
                        return AggregationExpressionCriteria.whereExpr(
                                ComparisonOperators.valueOf("$salePrice")
                                        .equalTo("$regularPrice")
                        );
                    }
                });
    }

    public Optional<TextCriteria> buildTextCriteria(final ProductFilter filter) {
        return Optional.ofNullable(filter.getTerm())
                .filter(t -> !t.isBlank())
                .map(TextCriteria.forDefaultLanguage()::matching);
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

}
