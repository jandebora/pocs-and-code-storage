package com.ccbravo.pocs.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ccbravo.pocs.domain.MongoEntity;

@Profile("mongodb")
@Repository
public interface MongoDBRepository extends MongoRepository<MongoEntity, String> {

}
