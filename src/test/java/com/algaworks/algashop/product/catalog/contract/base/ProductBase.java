package com.algaworks.algashop.product.catalog.contract.base;

import com.algaworks.algashop.product.catalog.application.ResourceNotFoundException;
import com.algaworks.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.algaworks.algashop.product.catalog.application.product.query.ProductQueryService;
import com.algaworks.algashop.product.catalog.presentation.ProductController;
import com.algaworks.algashop.product.catalog.utility.databuilder.ProductDetailOutputDataBuilder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
class ProductBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProductQueryService queryService;
    @MockitoBean
    private ProductManagementApplicationService applicationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(UTF_8).build());
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        when(queryService.findById(UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a")))
                .thenReturn(ProductDetailOutputDataBuilder.builder()
                        .withId(() -> UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a"))
                        .build());

        when(queryService.findById(UUID.fromString("21651a12-b126-4213-ac21-19f66ff4642e")))
                .thenThrow(new ResourceNotFoundException());

        final var page =  PageModel.<ProductDetailOutput>builder()
                .content(ProductDetailOutputDataBuilder.builder().buildList(2))
                .number(0)
                .totalPages(1)
                .totalElements(2)
                .build();
        when(queryService.filter(10, 0)).thenReturn(page);

        final var productId = UUID.randomUUID();
        when(applicationService.create(argThat(i -> i.getName().equals("Created"))))
                .thenReturn(productId);
        when(queryService.findById(productId))
                .thenReturn(ProductDetailOutputDataBuilder.builder()
                        .withId(() -> productId)
                        .withInStock(() -> false)
                        .build());
    }

}
