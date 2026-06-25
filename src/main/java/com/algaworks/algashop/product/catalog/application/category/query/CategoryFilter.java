package com.algaworks.algashop.product.catalog.application.category.query;

import com.algaworks.algashop.product.catalog.application.SortablePageFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CategoryFilter  extends SortablePageFilter<CategoryFilter.SortType> {

    @Nullable
    private final Boolean enabled;
    @Nullable
    private final String name;

    protected CategoryFilter(@Nullable
                             final Integer pageNumber,
                             @Nullable
                             final Integer pageSize,
                             final CategoryFilter.@Nullable SortType sortByProperty,
                             final Sort.@Nullable Direction sortDirection,
                             @Nullable
                             final Boolean enabled,
                             @Nullable
                             final String name) {
        super(pageNumber, pageSize, sortByProperty, sortDirection);
        this.enabled = enabled;
        this.name = name;
    }


    @Override
    public SortType getSortByPropertOrDefault() {
        return SortType.NAME;
    }


    @Getter
    @RequiredArgsConstructor
    public enum SortType {
        NAME("name");

        private final  String propertyName;
    }

}
