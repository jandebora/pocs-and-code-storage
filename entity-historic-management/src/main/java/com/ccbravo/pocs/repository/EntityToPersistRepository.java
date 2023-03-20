package com.ccbravo.pocs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccbravo.pocs.domain.EntityToPersist;

@Repository
public interface EntityToPersistRepository extends JpaRepository<EntityToPersist, Long> {

}
