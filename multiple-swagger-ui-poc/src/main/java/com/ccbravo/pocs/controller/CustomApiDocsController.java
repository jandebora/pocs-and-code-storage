package com.ccbravo.pocs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccbravo.pocs.service.CustomOpenAPIDocFromDatabaseIdService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/v1/customOpenAPIDocs")
public class CustomApiDocsController {
    
    @Autowired
    private CustomOpenAPIDocFromDatabaseIdService service;
    
    @GetMapping("/{id}")
    public Object getDocs(@PathVariable String id) throws JsonProcessingException {
        return service.getDocumentation(id);
    }
}
