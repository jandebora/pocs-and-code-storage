package com.ccbravo.pocs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ccbravo.pocs.domain.PostgreSQLEntity;
import com.ccbravo.pocs.services.PostgreSQLService;

import javax.validation.Valid;

@Profile("postgresql")
@Validated
@RestController
@RequestMapping(value = "/postgresqlEntities")
public class PostgreSQLController {

    @Autowired
    private PostgreSQLService service;
    
    @GetMapping
    public List<PostgreSQLEntity> getAll() {
        return service.getAll();
    }
    
    @GetMapping("/{id}")
    public PostgreSQLEntity findById(@PathVariable Long id) {
        return service.findById(id);
    }
    
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody PostgreSQLEntity entity) {
        Long id = service.create(entity);
        final var locationUri =
                MvcUriComponentsBuilder.fromMethodCall(
                                MvcUriComponentsBuilder.on(this.getClass()).findById(id))
                        .build()
                        .toUri();
        
        return ResponseEntity.created(locationUri).build();
    }
}
