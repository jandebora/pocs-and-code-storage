package com.ccbravo.pocs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccbravo.pocs.model.MessageDTO;
import com.ccbravo.pocs.model.MessageSumDTO;
import com.ccbravo.pocs.service.Service1Consumer;
import com.ccbravo.pocs.service.Service2;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class Service2Impl implements Service2 {
    
    @Autowired
    private Service1Consumer service1Consumer;

    @Override
    public MessageSumDTO getMessageSumAsync() {
        log.info("Calling synchronous messageOne URI");
        MessageDTO messageOne = service1Consumer.getMessageOne().block();
        
        log.info("Calling asynchronous execute URI");
        service1Consumer.execute().subscribe();
        
        log.info("Calling synchronous messageTwo URI");
        MessageDTO messageTwo = service1Consumer.getMessageTwo().block();
        
        log.info("Calling asynchronous execute URI");
        service1Consumer.execute().subscribe();
        
        return createSum(messageOne, messageTwo);
    }

    @Override
    public MessageSumDTO getMessageSumSync() {
        log.info("Calling synchronous messageOne URI");
        MessageDTO messageOne = service1Consumer.getMessageOne().block();
        
        log.info("Calling synchronous execute URI");
        service1Consumer.execute().block();
        
        log.info("Calling synchronous messageTwo URI");
        MessageDTO messageTwo = service1Consumer.getMessageTwo().block();
        
        log.info("Calling synchronous execute URI");
        service1Consumer.execute().block();
        
        return createSum(messageOne, messageTwo);
    }
    
    private MessageSumDTO createSum(MessageDTO messageOne, MessageDTO messageTwo) {
        List<String> ids = new ArrayList<>();
        ids.add(messageOne.getId());
        ids.add(messageTwo.getId());
        
        String messageSum = messageOne.getMessage() + " " + messageTwo.getMessage();
        
        return MessageSumDTO.builder()
                .ids(ids)
                .messageSum(messageSum)
                .build();
    }

}
