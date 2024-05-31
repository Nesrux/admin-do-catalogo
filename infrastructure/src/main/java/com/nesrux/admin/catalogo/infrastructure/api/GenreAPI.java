package com.nesrux.admin.catalogo.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
@RequestMapping("genres")
@Tag(name = "Genre")
public interface GenreAPI {
    
    ResponseEntity<?> create(@RequestBody ){}
}
