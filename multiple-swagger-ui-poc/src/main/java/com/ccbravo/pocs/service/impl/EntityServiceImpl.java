package com.ccbravo.pocs.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.ccbravo.pocs.mapper.EntityMapper;
import com.ccbravo.pocs.model.EntityDTO;
import com.ccbravo.pocs.repository.EntityRepository;
import com.ccbravo.pocs.service.EntityService;

@Service 
public class EntityServiceImpl implements EntityService {
    
    @Autowired
    private EntityMapper mapper;
    
    @Autowired
    private EntityRepository repository;

    @Override
    public void createEntity(EntityDTO entity) {
        repository.save(mapper.toDomain(entity));
        
    }

    @Override
    public List<EntityDTO> getEntities() {
        return repository.findAll().stream().map(e -> mapper.toDto(e)).toList();
    }

    @Override
    public EntityDTO getEntity(String id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new NotFoundException("")));
    }

}
