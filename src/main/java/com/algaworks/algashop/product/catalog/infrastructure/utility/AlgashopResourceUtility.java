package com.algaworks.algashop.product.catalog.infrastructure.utility;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.isNull;

public class AlgashopResourceUtility {

    public static String readContent(final String resourceName) {
        try (final var inputStream = ResourceUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (isNull(inputStream)) {
                throw new RuntimeException(new FileNotFoundException(resourceName));
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}
