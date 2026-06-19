package com.algaworks.algashop.product.catalog.utility;

import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Slugfier {
	
	private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
	private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

	@Nullable
	public static String slugify(@Nullable final String text) {
		if (isNull(text)) {
			return null;
		}
		final var noWhitespace = WHITESPACE.matcher(text).replaceAll("-");
		final var normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
		final var slug = NON_LATIN.matcher(normalized).replaceAll("");
		return slug.toLowerCase(Locale.ENGLISH);
	}
	
}