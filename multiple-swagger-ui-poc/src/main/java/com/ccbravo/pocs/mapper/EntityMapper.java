package com.ccbravo.pocs.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ccbravo.pocs.controller.CustomApiDocsController;
import com.ccbravo.pocs.domain.Entity;
import com.ccbravo.pocs.model.EntityDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

@Mapper
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);
    
    EntityDTO toDto(Entity entity);
    
    Entity toDomain(EntityDTO dto);
    
    @AfterMapping
    default void saveUriReference(Entity entity, @MappingTarget EntityDTO dto) {
        try {
            dto.setUri(MvcUriComponentsBuilder.fromMethodCall(
                    MvcUriComponentsBuilder.on(CustomApiDocsController.class).getDocs(dto.getId())).build().toUriString());
        } catch (JsonProcessingException e) {
            dto.setUri("");
        }
    }
}
