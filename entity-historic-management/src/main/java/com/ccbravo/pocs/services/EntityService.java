package com.ccbravo.pocs.services;

import java.util.List;

import com.ccbravo.pocs.domain.EntityToPersist;

public interface EntityService {

    EntityToPersist getById(Long id);
    List<EntityToPersist> getList();
    Long save(EntityToPersist entity); 
    void update(Long id, EntityToPersist entity);
}
