package com.algaworks.algashop.product.catalog.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Optional;

import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class SortablePageFilter<T> extends PageFilter {

    @Nullable
    @Getter(PROTECTED)
    private T sortByProperty;
    @Getter(PROTECTED)
    @Nullable
    private Direction sortDirection;

    protected SortablePageFilter(@Nullable
                                 final Integer pageNumber,
                                 @Nullable
                                 final Integer pageSize,
                                 @Nullable
                                 final T sortByProperty,
                                 @Nullable
                                 final Direction sortDirection) {
        super(isNull(pageSize) ? 15 : pageSize, isNull(pageNumber) ? 0 : pageNumber);
        this.sortByProperty = sortByProperty;
        this.sortDirection = sortDirection;
    }

    public abstract T getSortByPropertOrDefault();

    public Sort.Direction getSortDirectionOrDefault() {
        return Optional.ofNullable(getSortDirection()).orElse(Sort.Direction.ASC);
    }

}
