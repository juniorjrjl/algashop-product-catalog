package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoryQueryService {

    public PageModel<CategoryDetailOutput> filter(final Integer size, final Integer number){
        return null;
    }

    public CategoryDetailOutput findById(final UUID categoryId){
        return null;
    }

}
