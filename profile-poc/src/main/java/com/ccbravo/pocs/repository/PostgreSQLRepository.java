package com.ccbravo.pocs.repository;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccbravo.pocs.domain.PostgreSQLEntity;

@Profile("postgresql")
@Repository
public interface PostgreSQLRepository extends JpaRepository<PostgreSQLEntity, Long>{

}
