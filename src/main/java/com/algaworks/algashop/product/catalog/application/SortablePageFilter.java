package com.algaworks.algashop.product.catalog.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort.Direction;

import static lombok.AccessLevel.PROTECTED;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class SortablePageFilter<T> extends PageFilter {

    @Nullable
    @Getter(PROTECTED)
    private T sortByProperty;
    @Getter(PROTECTED)
    @Nullable
    private Direction sortDirection;

    protected SortablePageFilter(final Integer pageNumber, final Integer pageSize) {
        super(pageSize, pageNumber);
    }

    public abstract T getSortByPropertOrDefault();

    public abstract Direction getSortDirectionOrDefault();

}
