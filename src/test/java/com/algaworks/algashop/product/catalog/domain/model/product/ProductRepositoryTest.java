package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoadProperties;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoader;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.product.QuantityInStockAdjustmentMongoImpl;
import com.algaworks.algashop.product.catalog.utility.extesion.MongoContainer;
import com.algaworks.algashop.product.catalog.utility.extesion.MongoDBTestContainerExtension;
import com.algaworks.algashop.product.catalog.utility.tag.IntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.mongodb.MongoDBContainer;

@SpringBootTest
@ExtendWith(MongoDBTestContainerExtension.class)
@Import({
        MongoConfig.class,
        QuantityInStockAdjustmentMongoImpl.class,
        DataLoader.class
})
@EnableConfigurationProperties(DataLoadProperties.class)
@IntegrationTest
@Slf4j
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @MongoContainer
    private static MongoDBContainer mongoDBContainer;

    @DynamicPropertySource
    public static void configurePropertySource(final DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    public void shouldFilter() {
        final var products = productRepository
                .findAllByEnabled(true, PageRequest.of(0, 3));
        products.forEach(p -> log.info("Product - Id: {} Name: {}", p.id(), p.name()));
    }

}