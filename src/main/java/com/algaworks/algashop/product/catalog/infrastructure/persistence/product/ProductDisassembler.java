package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import com.algaworks.algashop.product.catalog.utility.Slugfier;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@AnnotateWith(NullMarked.class)
@Mapper(componentModel = SPRING, imports = { Slugfier.class, StringUtils.class})
public interface ProductDisassembler {

    @Mapping(target = "slug", expression = "java(Slugfier.slugify(domainModel.getName()))")
    @Mapping(target = "quantityInStock", source = "stockAmount")
    ProductDetailOutput toDetailOutput(final Product domainModel);

    @Mapping(target = "shortDescription", expression = "java(StringUtils.abbreviate(domainModel.getDescription(), 15))")
    @Mapping(target = "slug", expression = "java(Slugfier.slugify(domainModel.getName()))")
    @Mapping(target = "quantityInStock", source = "stockAmount")
    ProductSummaryOutput toSummaryOutput(final Product domainModel);


    PageModel<ProductSummaryOutput> toPagedSummaryOutput(final Page<Product> page);

}
