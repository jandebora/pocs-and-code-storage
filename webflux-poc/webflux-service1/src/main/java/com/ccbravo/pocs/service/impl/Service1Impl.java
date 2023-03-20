package com.ccbravo.pocs.service.impl;

import org.springframework.stereotype.Service;

import com.ccbravo.pocs.model.MessageDTO;
import com.ccbravo.pocs.service.Service1;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Service1Impl implements Service1 {

    public MessageDTO getMessageNumberOne() {
        log.info("Getting Message number one");
        
        return MessageDTO.builder()
                .message("message number one")
                .build();
    }

    public MessageDTO getMessageNumberTwo() {
        log.info("Getting Message number two");
        
        return MessageDTO.builder()
                .message("message number two")
                .build();
    }

    public void executeTimer() {
        log.info("Executing Timer");
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Error During Sleep: ", e);
            Thread.currentThread().interrupt();
        }
    }

}
