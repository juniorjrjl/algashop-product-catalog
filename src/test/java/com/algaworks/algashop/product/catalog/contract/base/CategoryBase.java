package com.algaworks.algashop.product.catalog.contract.base;

import com.algaworks.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.algaworks.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.algaworks.algashop.product.catalog.application.product.query.PageModel;
import com.algaworks.algashop.product.catalog.presentation.CategoryController;
import com.algaworks.algashop.product.catalog.utility.databuilder.CategoryDetailOutputDataBuilder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CategoryQueryService queryService;
    @MockitoBean
    private CategoryManagementApplicationService applicationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(UTF_8).build());
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        findByIdMocks();
        filterMocks();
        insertMocks();
        updateMocks();
        deleteMocks();
    }

    private void deleteMocks() {
        doNothing().when(applicationService)
                .disable(UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2"));
    }

    private void updateMocks() {
        final var categoryId = UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2");
        doNothing().when(applicationService).update(
                eq(categoryId),
                any()
                );
        when(queryService.findById(categoryId))
                .thenReturn(CategoryDetailOutputDataBuilder.builder()
                        .withId(() -> categoryId)
                        .build());
    }

    private void insertMocks() {
        final var categoryId = UUID.randomUUID();
        when(applicationService.create(any()))
                .thenReturn(categoryId);
        when(queryService.findById(categoryId))
                .thenReturn(CategoryDetailOutputDataBuilder.builder().build());
    }

    private void filterMocks() {
        final var page = PageModel.<CategoryDetailOutput>builder()
                .number(0)
                .size(2)
                .totalElements(2)
                .totalPages(1)
                .content(CategoryDetailOutputDataBuilder.builder().buildList(2))
                .build();
        when(queryService.filter(any(), any()))
                .thenReturn(page);
    }

    private void findByIdMocks() {
        when(queryService.findById(UUID.fromString("f5ab7a1e-37da-41e1-892b-a1d38275c2f2")))
                .thenReturn(CategoryDetailOutputDataBuilder.builder().build());
    }

}
