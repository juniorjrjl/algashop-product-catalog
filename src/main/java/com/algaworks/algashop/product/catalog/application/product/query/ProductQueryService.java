package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.product.ProductDisassembler;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository repository;
    private final ProductDisassembler disassembler;

    public ProductDetailOutput findById(final UUID id){
        return repository.findById(id)
                .map(disassembler::toDetailOutput)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public PageModel<ProductDetailOutput> filter(final Integer size, final Integer number){
        return null;
    }

}
