package com.algaworks.algashop.product.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ProductCatalogApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));
		SpringApplication.run(ProductCatalogApplication.class, args);
	}

}
