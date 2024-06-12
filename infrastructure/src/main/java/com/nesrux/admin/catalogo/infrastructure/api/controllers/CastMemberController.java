package com.nesrux.admin.catalogo.infrastructure.api.controllers;

import com.nesrux.admin.catalogo.domain.pagination.Pagination;
import com.nesrux.admin.catalogo.infrastructure.api.CastMemberAPI;
import org.springframework.http.ResponseEntity;

public class CastMemberController implements CastMemberAPI {
    @Override
    public ResponseEntity<?> create(Object input) {
        return null;
    }

    @Override
    public Pagination<Object> listCastMembers(String search, int page, int perPage, String sort, String dir) {
        return null;
    }

    @Override
    public Object getById(String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateById(String id, Object input) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
}
