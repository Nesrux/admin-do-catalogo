package com.nesrux.admin.catalogo.infrastructure.genre.persistence;

import java.util.Objects;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.nesrux.admin.catalogo.domain.category.CategoryId;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {
    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(final GenreJpaEntity aGenre, final CategoryId aCategoryId) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryId.getValue());
    };

    public static GenreCategoryJpaEntity from(final GenreJpaEntity aGenre, final CategoryId aCategoryId) {
        return new GenreCategoryJpaEntity(aGenre, aCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final GenreCategoryJpaEntity other = (GenreCategoryJpaEntity) obj;
        return Objects.equals(getId(), other.getId());
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJpaEntity genre) {
        this.genre = genre;
    }
    

}
