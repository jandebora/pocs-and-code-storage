package com.ccbravo.pocs.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.ccbravo.pocs.domain.PostgreSQLEntity;
import com.ccbravo.pocs.repository.PostgreSQLRepository;
import com.ccbravo.pocs.services.PostgreSQLService;

import javax.persistence.EntityNotFoundException;

@Profile("postgresql")
@Service
public class PostgreSQLServiceImpl implements PostgreSQLService {
    
    @Autowired
    private PostgreSQLRepository repository;

    @Override
    public Long create(PostgreSQLEntity entity) {
        return repository.save(entity).getId();
    }

    @Override
    public List<PostgreSQLEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public PostgreSQLEntity findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(""));
    }

}
