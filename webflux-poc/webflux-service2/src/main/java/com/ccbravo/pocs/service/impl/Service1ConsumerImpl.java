package com.ccbravo.pocs.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ccbravo.pocs.configuration.Service2ConsumerConfigProperties;
import com.ccbravo.pocs.model.MessageDTO;
import com.ccbravo.pocs.service.Service1Consumer;

import reactor.core.publisher.Mono;

@Service
public class Service1ConsumerImpl implements Service1Consumer {
    
    @Autowired
    private Service2ConsumerConfigProperties service2ConfigProperties;

    private WebClient webClient;
    
    @PostConstruct
    public void init() {
        WebClient.Builder webClientBuilder = WebClient.builder().clone();
        this.webClient = webClientBuilder.baseUrl(service2ConfigProperties.getService1().getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();
    }

    @Override
    public Mono<MessageDTO> getMessageOne() {
        return getMessage("one");
    }

    @Override
    public Mono<MessageDTO> getMessageTwo() {
        return getMessage("two");
    }

    @Override
    public Mono<Void> execute() {
        return this.webClient.get()
                .uri(service2ConfigProperties.getService1().getPaths().getMessages().concat("execute"))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }
    
    private Mono<MessageDTO> getMessage(String messageId) {
        return this.webClient.get()
                .uri(service2ConfigProperties.getService1().getPaths().getMessages().concat(messageId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<MessageDTO>() {});
    }
    
}
