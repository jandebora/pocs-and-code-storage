package com.ccbravo.pocs.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.ccbravo.pocs.domain.MongoEntity;
import com.ccbravo.pocs.repository.MongoDBRepository;
import com.ccbravo.pocs.services.MongoDBService;

import javax.persistence.EntityNotFoundException;

@Profile("mongodb")
@Service
public class MongoDBServiceImpl implements MongoDBService {
    
    @Autowired
    private MongoDBRepository repository;

    @Override
    public String create(MongoEntity entity) {
        return repository.save(entity).getId();
    }

    @Override
    public List<MongoEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public MongoEntity findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
    }

}
