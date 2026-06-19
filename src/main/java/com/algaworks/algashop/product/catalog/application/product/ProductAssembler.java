package com.algaworks.algashop.product.catalog.application.product;

import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@AnnotateWith(NullMarked.class)
@Mapper(componentModel = SPRING)
public interface ProductAssembler {

    @Mapping(target = "name", source = "input.name")
    @Mapping(target = "enabled", source = "input.enabled")
    Product toDomainModel(final ProductInput input, final Category category);

    @Mapping(target = "name", source = "input.name")
    @Mapping(target = "enabled", source = "input.enabled")
    void updatedDomainModel(final ProductInput input,
                            final Category category,
                            @MappingTarget
                            final Product product);


}
