package com.algaworks.algashop.product.catalog.domain.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;

@Getter
@ToString
@Builder
public class ProductDelistedEvent {
	private UUID productId;
	@Builder.Default
	private OffsetDateTime delistedAt = OffsetDateTime.now(UTC);
}