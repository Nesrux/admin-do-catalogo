package com.nesrux.admin.catalogo.domain.category;

import com.nesrux.admin.catalogo.domain.AggregateRoot;
import com.nesrux.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryId> implements  Cloneable {

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

    public static Category newCategory(final String Aname, final String aDesciption, final boolean isActive) {
        final var id = CategoryId.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, Aname, aDesciption, isActive, now, now, deletedAt);
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String Aname, final String aDescription,
                           final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = Aname;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;

    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
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

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
