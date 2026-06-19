package com.algaworks.algashop.product.catalog.application.product;

import com.algaworks.algashop.product.catalog.application.product.management.ProductInput;
import com.algaworks.algashop.product.catalog.domain.model.product.Product;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@AnnotateWith(NullMarked.class)
@Mapper(componentModel = SPRING)
public interface ProductAssembler {

    Product toDomainModel(final ProductInput input);

    void updatedDomainModel(final ProductInput input,
                            @MappingTarget
                            final Product product);

}
