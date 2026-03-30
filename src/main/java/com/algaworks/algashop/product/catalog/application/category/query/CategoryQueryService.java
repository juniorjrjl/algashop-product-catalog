package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;

import java.util.UUID;

public interface CategoryQueryService {

    PageModel<CategoryDetailOutput> filter(final Integer size, final Integer number);

    CategoryDetailOutput findById(final UUID categoryId);

}
