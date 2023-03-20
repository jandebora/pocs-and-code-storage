package com.ccbravo.pocs.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccbravo.pocs.model.EntityDTO;
import com.ccbravo.pocs.service.EntityService;

@RestController
@RequestMapping(value = "/v1/entities")
public class GenericEntityController {
    
    @Autowired
    private EntityService service;

    @GetMapping
    public List<EntityDTO> getList() {
        return service.getEntities();
    }
    
    @PostMapping
    public void createData(@RequestBody EntityDTO entity) {
        service.createEntity(entity);
    }
}
