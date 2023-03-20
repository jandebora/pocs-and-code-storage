package com.ccbravo.pocs.services;

import java.util.List;

import com.ccbravo.pocs.domain.MongoEntity;

public interface MongoDBService {

    String create(MongoEntity entity);
    List<MongoEntity> getAll();
    MongoEntity findById(String id);
}
