package com.ccbravo.pocs.services;

import java.util.List;

import com.ccbravo.pocs.domain.PostgreSQLEntity;

public interface PostgreSQLService {

    Long create(PostgreSQLEntity entity);
    List<PostgreSQLEntity> getAll();
    PostgreSQLEntity findById(Long id);
}
