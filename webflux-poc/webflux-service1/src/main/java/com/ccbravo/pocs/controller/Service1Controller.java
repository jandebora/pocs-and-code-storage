package com.ccbravo.pocs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ccbravo.pocs.model.MessageDTO;
import com.ccbravo.pocs.service.Service1;

import io.swagger.v3.oas.annotations.tags.Tag;

@Validated
@Tag(name = "Service1Controller", description = "Service1Controller API")
@RestController
@RequestMapping("/v1")
public class Service1Controller {

    @Autowired
    private Service1 service1;
    
    @GetMapping("/messages/one")
    public MessageDTO getMessageOne() {
        
        return service1.getMessageNumberOne();
    }
    
    @GetMapping("/messages/two")
    public MessageDTO getMessageTwo() {
        
        return service1.getMessageNumberTwo();
    }
    
    @GetMapping("/messages/execute")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void executeTimer() {
        
        service1.executeTimer();
    }
}
