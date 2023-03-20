package com.ccbravo.pocs.service;

import com.ccbravo.pocs.model.MessageDTO;

import reactor.core.publisher.Mono;

public interface Service1Consumer {

    Mono<MessageDTO> getMessageOne();
    Mono<MessageDTO> getMessageTwo();
    Mono<Void> execute();
}
