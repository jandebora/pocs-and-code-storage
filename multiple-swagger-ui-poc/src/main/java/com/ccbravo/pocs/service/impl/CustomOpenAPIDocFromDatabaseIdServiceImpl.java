package com.ccbravo.pocs.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import com.ccbravo.pocs.configuration.DetailGroupOpenApiResource;
import com.ccbravo.pocs.domain.Entity;
import com.ccbravo.pocs.repository.EntityRepository;
import com.ccbravo.pocs.service.CustomOpenAPIDocFromDatabaseIdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOpenAPIDocFromDatabaseIdServiceImpl implements CustomOpenAPIDocFromDatabaseIdService {

    @Autowired
    private DetailGroupOpenApiResource resource;
    
    @Autowired
    private EntityRepository repository;

    @Override
    @SneakyThrows
    public String getDocumentation(String id) {
        List<String> ids = repository.findAll().stream().map(Entity::getId).toList();
        
        if (!ids.contains(id)) 
            throw new NotFoundException("No se ha encontrado el identificador.");

        try {
            Gson gson = new Gson();
            OpenAPI openAPI = gson.fromJson(resource.getOpenApiJson(), OpenAPI.class);
            Map<String, PathItem> map = openAPI.getPaths();
            Collection<PathItem> pathItems = map.values();
            
            pathItems.forEach(pathItem ->  
                pathItem.readOperations().forEach(operation -> {
                    List<Parameter> parameters = operation.getParameters().stream()
                            .filter(parameter -> parameter.getName().equals("id")).toList();
                    operation.getParameters().removeAll(parameters);
                }));
            
            return Pattern.compile("{id}", Pattern.LITERAL).matcher(gson.toJson(openAPI)).replaceAll(id);
        } catch (JsonProcessingException e) {
            log.error("Ha ocurrido un error");
            return "";
        }
    }
    
    
}
