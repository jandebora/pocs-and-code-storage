package com.ccbravo.pocs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ccbravo.pocs.model.EntityDTO;
import com.ccbravo.pocs.service.EntityService;

@RestController
@RequestMapping(value = "/v1/entities/{id}")
public class DetailEntityController {
    
    @Autowired
    private EntityService service;

    @GetMapping
    public EntityDTO getDetail(@PathVariable String id, @RequestParam(required=false) String prueba) {
        return service.getEntity(id);
    }
}
