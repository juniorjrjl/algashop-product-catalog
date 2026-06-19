package com.algaworks.algashop.product.catalog.domain.model.category;

import com.algaworks.algashop.product.catalog.domain.model.IdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@Document(collection = "categories")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Category {

    @Id
    private UUID id;
    private String name;
    @Setter
    private boolean enabled;
    @CreatedDate
    private OffsetDateTime createdAt;
    @CreatedBy
    private UUID createdByUserId;
    @LastModifiedDate
    private OffsetDateTime updatedAt;
    @LastModifiedBy
    private UUID lastModifiedByUserId;
    @Version
    private Long version;

    public Category(final String name, final boolean enabled) {
        this.id = IdGenerator.generateTimeBasedUUID();
        this.setName(name);
        this.setEnabled(enabled);
    }

    public void setName(final String name) {
        if (name.isBlank()){
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Category category)) return false;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
