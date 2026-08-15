package com.algaworks.algashop.product.catalog.domain.model.product;

import com.algaworks.algashop.product.catalog.infrastructure.persistence.MongoConfig;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoadProperties;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.dataload.DataLoader;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.product.QuantityInStockAdjustmentMongoImpl;
import com.algaworks.algashop.product.catalog.infrastructure.persistence.product.StockUpdateFailerException;
import com.algaworks.algashop.product.catalog.utility.CustomFaker;
import com.algaworks.algashop.product.catalog.utility.tag.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@Import({
        MongoConfig.class,
        QuantityInStockAdjustmentMongoImpl.class,
        DataLoader.class
})
@EnableConfigurationProperties(DataLoadProperties.class)
@IntegrationTest
class QuantityInStockAdjustmentTest {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    private static final UUID existingProductId = UUID.fromString("946cea3b-d11d-4f11-b88d-3089b4e74087");

    @Autowired
    private QuantityInStockAdjustment quantityInStockAdjustment;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataLoader dataLoader;


    @BeforeEach
    void setUp() throws Exception {
        dataLoader.run(new DefaultApplicationArguments());
        CustomFaker.getInstance().reseed();
    }

    @Test
    void shouldIncreaseQuantity() {
        final var product = productRepository.findById(existingProductId).orElseThrow();

        final var stockAmount = customFaker.number().randomDigitNotZero();
        quantityInStockAdjustment.increase(existingProductId, stockAmount);
        quantityInStockAdjustment.increase(existingProductId, stockAmount);

        final var updatedProduct = productRepository.findById(existingProductId).orElseThrow();
        assertThat(updatedProduct.getStockAmount()).isEqualTo(stockAmount * 2 + product.getStockAmount());
        assertThat(updatedProduct.getVersion()).isEqualTo(product.getVersion() + 2);
    }

    @Test
    void shouldDecreaseQuantity() {
        final var product = productRepository.findById(existingProductId).orElseThrow();

        final var stockAmount = customFaker.number().numberBetween(1, product.getStockAmount() / 2);
        quantityInStockAdjustment.decrease(existingProductId, stockAmount);
        quantityInStockAdjustment.decrease(existingProductId, stockAmount);

        final var updatedProduct = productRepository.findById(existingProductId).orElseThrow();
        final var expectedAmount = product.getStockAmount() - (stockAmount * 2);
        assertThat(updatedProduct.getStockAmount()).isEqualTo(expectedAmount);
        assertThat(updatedProduct.getVersion()).isEqualTo(product.getVersion() + 2);
    }

    @Test
    void shouldNotDecreaseQuantity(){
        final var product = productRepository.findById(existingProductId).orElseThrow();
        assertThatThrownBy(() -> quantityInStockAdjustment.decrease(existingProductId, 100))
                .isInstanceOf(StockUpdateFailerException.class);

        final var productAfterUpdate = productRepository.findById(existingProductId).orElseThrow();
        assertThat(productAfterUpdate.getStockAmount()).isEqualTo(product.getStockAmount());

    }

    @Test
    void shouldCalculate(){
        final var product = productRepository.findById(existingProductId).orElseThrow();

        final var stockAmount = customFaker.number().numberBetween(1, product.getStockAmount());
        final var actual = quantityInStockAdjustment.decrease(existingProductId, stockAmount);

        assertThat(actual.productId()).isEqualTo(existingProductId);
        assertThat(actual.previousStockAmount()).isEqualTo(product.getStockAmount());
        assertThat(actual.newStockAmount()).isEqualTo(product.getStockAmount() - stockAmount);
    }

}
