package com.ccbravo.pocs.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.ccbravo.pocs.domain.EntityToPersist;
import com.ccbravo.pocs.repository.EntityToPersistRepository;
import com.ccbravo.pocs.services.EntityService;

@Service
public class EntityServiceImpl implements EntityService {
    
    @Autowired
    private EntityToPersistRepository repository;

    @Override
    public EntityToPersist getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Not Found"));
    }

    @Override
    public List<EntityToPersist> getList() {
        return repository.findAll();
    }

    @Override
    public Long save(EntityToPersist entity) {
        return repository.save(entity).getId();
    }

    @Override
    public void update(Long id, EntityToPersist entity) {
        EntityToPersist entityToPersist = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Found"));
        entityToPersist.setName(entity.getName());
        
        repository.save(entityToPersist);
    }

}
