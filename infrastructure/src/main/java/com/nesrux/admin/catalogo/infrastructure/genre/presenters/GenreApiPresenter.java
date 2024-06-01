package com.nesrux.admin.catalogo.infrastructure.genre.presenters;

import com.nesrux.admin.catalogo.application.genre.retrive.get.GenreOutput;
import com.nesrux.admin.catalogo.application.genre.retrive.list.GenreListOutput;
import com.nesrux.admin.catalogo.infrastructure.genre.models.GenreListResponse;
import com.nesrux.admin.catalogo.infrastructure.genre.models.GenreResponse;

public class GenreApiPresenter {
   public static GenreResponse present(final GenreOutput out) {
        return new GenreResponse(
                out.id(),
                out.name(),
                out.categories(),
                out.isActive(),
                out.createdAt(),
                out.updatedAt(),
                out.deletedAt());
    }

  public  static GenreListResponse present(final GenreListOutput out) {
        return new GenreListResponse(
                out.id(),
                out.name(),
                out.isActive(),
                out.createdAt(),
                out.deletedAt());
    }
}
