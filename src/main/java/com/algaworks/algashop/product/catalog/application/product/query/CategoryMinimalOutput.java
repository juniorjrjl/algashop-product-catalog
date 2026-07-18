package com.algaworks.algashop.product.catalog.application.product.query;

import com.algaworks.algashop.product.catalog.utility.Slugfier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMinimalOutput {

    private UUID id;
    private String name;
    private boolean enabled;

    @Nullable
    public String getSlug(){
        return Slugfier.slugify(this.name);
    }

}
