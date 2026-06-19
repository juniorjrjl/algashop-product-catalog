package com.algaworks.algashop.product.catalog.infrastructure.persistence.cateogory;


import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.domain.model.category.Category;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@AnnotateWith(NullMarked.class)
@Mapper(componentModel = SPRING)
public interface CategoryDisassembler {

    CategoryDetailOutput toDetailOutput(final Category domain);

}
