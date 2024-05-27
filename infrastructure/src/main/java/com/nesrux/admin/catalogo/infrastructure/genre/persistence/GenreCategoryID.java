package com.nesrux.admin.catalogo.infrastructure.genre.persistence;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GenreCategoryID {
    @Column(name = "genre_id", nullable = false)
    private String genreId;
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    public GenreCategoryID() {
    }

    private GenreCategoryID(final String genreId, final String categoryId) {
        this.genreId = genreId;
        this.categoryId = categoryId;
    }

    public static GenreCategoryID from(final String genreId, final String categoryId) {
        return new GenreCategoryID(genreId, categoryId);
    }

    public String getGenreId() {
        return genreId;
    }

    public void setGenreId(String genreId) {
        this.genreId = genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, categoryId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenreCategoryID other = (GenreCategoryID) obj;
        return Objects.equals(genreId, other.genreId) && Objects.equals(categoryId, other.categoryId);
    }

    

}
