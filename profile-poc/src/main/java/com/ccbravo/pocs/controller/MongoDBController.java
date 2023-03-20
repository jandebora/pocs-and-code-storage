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

import com.ccbravo.pocs.domain.MongoEntity;
import com.ccbravo.pocs.services.MongoDBService;

import javax.validation.Valid;

@Profile("mongodb")
@Validated
@RestController
@RequestMapping(value = "/mongoEntities")
public class MongoDBController {

    @Autowired
    private MongoDBService service;
    
    @GetMapping
    public List<MongoEntity> getAll() {
        return service.getAll();
    }
    
    @GetMapping("/{id}")
    public MongoEntity findById(@PathVariable String id) {
        return service.findById(id);
    }
    
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody MongoEntity entity) {
        String id = service.create(entity);
        final var locationUri =
                MvcUriComponentsBuilder.fromMethodCall(
                                MvcUriComponentsBuilder.on(this.getClass()).findById(id))
                        .build()
                        .toUri();
        
        return ResponseEntity.created(locationUri).build();
    }
}
