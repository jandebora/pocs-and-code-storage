package com.ccbravo.pocs.service;

import java.util.List;

import com.ccbravo.pocs.model.EntityDTO;

public interface EntityService {

    void createEntity(EntityDTO entity);
    List<EntityDTO> getEntities();
    EntityDTO getEntity(String id);
}
