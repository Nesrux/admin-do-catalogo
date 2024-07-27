package com.nesrux.admin.catalogo.infrastructure.video.persistence;

import com.nesrux.admin.catalogo.domain.video.Rating;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

    /*Essa Classe serve para fazer a conversão de um enum para um outro objeto qualquer
     * para ela funcionar é preciso que a enumeraçãp não esteja anotada com o @Enumereted(...)
     * O proprio Spring vai fazer a conversão toda vez que a classe for serializada ou desecializada*/

    @Override
    public String convertToDatabaseColumn(final Rating attribute) {
        if (attribute == null) return null;
        return attribute.getName();
    }

    @Override
    public Rating convertToEntityAttribute(final String dbData) {
        if (dbData == null) return null;
        return Rating.of(dbData).orElse(null);
    }
}
