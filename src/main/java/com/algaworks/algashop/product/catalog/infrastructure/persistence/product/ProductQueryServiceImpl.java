package com.algaworks.algashop.product.catalog.infrastructure.persistence.product;

import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductFilter;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.ProductSummaryOutput;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductNotFoundException;
import com.algaworks.algashop.product.catalog.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository repository;
    private final ProductDisassembler disassembler;

    @Override
    public ProductDetailOutput findById(final UUID id){
        return repository.findById(id)
                .map(disassembler::toDetailOutput)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public PageModel<ProductSummaryOutput> filter(final ProductFilter filter){
        final var domain = repository.findAll(PageRequest.of(filter.getPage(), filter.getSize()));
        return disassembler.toPagedSummaryOutput(domain);
    }

}
