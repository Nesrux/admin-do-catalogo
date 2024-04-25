package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.AggregateRoot;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryId> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryId anId,
                     final String aName,
                     final String aDescription,
                     final boolean isActive,
                     final Instant aCreationDate,
                     final Instant aUpdateDate,
                     final Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.deletedAt = aDeleteDate;

    }

    public static Category newCategory(final String Aname, final String aDesciption, final boolean isAtive) {
        final var id = CategoryId.unique();
        final var now = Instant.now();
        return new Category(id, Aname, aDesciption, isAtive, now, now, null);
    }

    public CategoryId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
