package com.ccbravo.pocs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ccbravo.pocs.domain.EntityToPersist;
import com.ccbravo.pocs.services.EntityService;

@RestController
@RequestMapping(value = "/v1/entities")
public class EntityController {
    
    @Autowired
    private EntityService service;

    @GetMapping
    public ResponseEntity<List<EntityToPersist>> getAll() {
        return ResponseEntity.ok(service.getList());
    }
    
    @GetMapping(value = "/{id}")
    public EntityToPersist findById(@PathVariable Long id) {
        return service.getById(id);
    }
    
    @PostMapping
    public ResponseEntity<Void> saveEntity(@RequestBody EntityToPersist entity) {
        Long id = service.save(entity);
        final var locationUri =
                MvcUriComponentsBuilder.fromMethodCall(
                                MvcUriComponentsBuilder.on(this.getClass()).findById(id))
                        .build()
                        .toUri();
        
        return ResponseEntity.created(locationUri).build();
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateEntity(@PathVariable Long id, @RequestBody EntityToPersist entity) {
        service.update(id, entity);
        
        return ResponseEntity.ok().build();
    }
}
