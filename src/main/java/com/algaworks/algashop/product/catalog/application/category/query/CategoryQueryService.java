package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface CategoryQueryService {

    PageModel<CategoryDetailOutput> filter(final CategoryFilter filter);

    CategoryDetailOutput findById(final UUID categoryId);

}
