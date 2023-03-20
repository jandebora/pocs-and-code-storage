package com.ccbravo.pocs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ccbravo.pocs.domain.Entity;

@Repository
public interface EntityRepository extends MongoRepository<Entity, String> {

}
