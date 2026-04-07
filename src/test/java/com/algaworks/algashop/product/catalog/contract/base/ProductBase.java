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
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(ProductController.class)
class ProductBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ProductQueryService queryService;
    @MockitoBean
    private ProductManagementApplicationService applicationService;

    @BeforeEach
    void setUp(final RestDocumentationContextProvider restDocumentation) {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .snippets().withTemplateFormat(TemplateFormats.asciidoctor())
                        .and().operationPreprocessors()
                        .withResponseDefaults(Preprocessors.prettyPrint())
                ).alwaysDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
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
                .disable(UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7b"));
        doThrow(ResourceNotFoundException.class).when(applicationService)
                .disable(UUID.fromString("21651a12-b126-4213-ac21-19f66ff4642f"));
    }

    private void updateMocks() {
        final var productId = UUID.fromString("019d3f21-af98-7db1-8bb7-b248dc05a4a3");
        doNothing().when(applicationService)
                .update(
                        eq(productId),
                        argThat(i -> i.getName().equals("Updated"))
                );
        when(queryService.findById(productId))
                .thenReturn(ProductDetailOutputDataBuilder.builder()
                        .withId(() -> productId)
                        .withInStock(() -> false)
                        .build());

        doThrow(ResourceNotFoundException.class).when(applicationService)
                .update(
                        eq(UUID.fromString("019d3f21-af98-7db1-8bb7-b248dc05a4a4")),
                        any()
                );
    }

    private void insertMocks() {
        final var productId = UUID.randomUUID();
        when(applicationService.create(argThat(i -> i.getName().equals("Created"))))
                .thenReturn(productId);
        when(queryService.findById(productId))
                .thenReturn(ProductDetailOutputDataBuilder.builder()
                        .withId(() -> productId)
                        .withInStock(() -> false)
                        .build());
    }

    private void filterMocks() {
        final var page = PageModel.<ProductDetailOutput>builder()
                .content(ProductDetailOutputDataBuilder.builder().buildList(2))
                .number(0)
                .totalPages(1)
                .totalElements(2)
                .build();
        when(queryService.filter(10, 0)).thenReturn(page);
    }

    private void findByIdMocks() {
        when(queryService.findById(UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a")))
                .thenReturn(ProductDetailOutputDataBuilder.builder()
                        .withId(() -> UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a"))
                        .build());

        when(queryService.findById(UUID.fromString("21651a12-b126-4213-ac21-19f66ff4642e")))
                .thenThrow(new ResourceNotFoundException());
    }

}
