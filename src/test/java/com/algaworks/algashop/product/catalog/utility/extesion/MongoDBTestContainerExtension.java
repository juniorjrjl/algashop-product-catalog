package com.algaworks.algashop.product.catalog.utility.extesion;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.mongodb.MongoDBContainer;

import java.lang.reflect.Modifier;
import java.time.Duration;

public class MongoDBTestContainerExtension implements BeforeAllCallback, BeforeEachCallback {

    private static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer("mongo:8.0.4")
                    .withReplicaSet()
                    .withStartupTimeout(Duration.ofSeconds(60));

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        mongoDBContainer.start();
        Class<?> testClass = context.getRequiredTestClass();
        for (final var field : testClass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers()) &&
                    field.getType().equals(MongoDBContainer.class) &&
                    field.isAnnotationPresent(MongoContainer.class)) {

                field.setAccessible(true);
                field.set(null, mongoDBContainer);
            }
        }
    }

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var mongoTemplate = SpringExtension
                .getApplicationContext(context)
                .getBean(MongoTemplate.class);

        mongoTemplate.remove(new Query(), "products");
        mongoTemplate.remove(new Query(), "categories");
    }

}
