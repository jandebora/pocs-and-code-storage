package com.ccbravo.pocs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccbravo.pocs.model.MessageSumDTO;
import com.ccbravo.pocs.service.Service2;

@RestController
@RequestMapping("/v1")
public class Service2Controller {

    @Autowired
    private Service2 service2;
    
    @GetMapping("/messageSums/sync")
    public MessageSumDTO getMessageSumSync() {
        return service2.getMessageSumSync();
    }
    
    @GetMapping("/messageSums/async")
    public MessageSumDTO getMessageSumAsync() {
        return service2.getMessageSumAsync();
    }
    
}
