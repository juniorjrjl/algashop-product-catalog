package com.algaworks.algashop.product.catalog.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageFilter {

    private Integer size = 15;
    private Integer page = 0;

}
