package com.ccbravo.pocs.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.ccbravo.pocs.configuration.Service2ConsumerConfigProperties;
import com.ccbravo.pocs.configuration.Service2ConsumerConfigProperties.Paths;
import com.ccbravo.pocs.configuration.Service2ConsumerConfigProperties.Service1Config;
import com.ccbravo.pocs.model.MessageDTO;
import com.ccbravo.pocs.service.impl.Service1ConsumerImpl;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class Service1ConsumerTest {
    
    @InjectMocks
    private Service1ConsumerImpl service;
    
    @Mock
    protected WebClient.RequestBodyUriSpec requestBodyUriMock;

    @SuppressWarnings("rawtypes")
    @Mock
    protected WebClient.RequestHeadersSpec requestHeadersMock;

    @SuppressWarnings("rawtypes")
    @Mock
    protected WebClient.RequestHeadersUriSpec requestHeadersUriMock;

    @Mock
    protected WebClient.RequestBodySpec requestBodyMock;

    @Mock
    protected WebClient.ResponseSpec responseMock;

    @Mock
    protected WebClient webClientMock;

    @Mock
    protected WebClient.Builder webClientBuilder;
    
    @Mock
    private Service2ConsumerConfigProperties configProperties;
    
    @BeforeEach
    public void init() {
        Service1Config service1Config = new Service1Config();
        Paths paths = new Paths();
        
        paths.setMessages("messages");
        service1Config.setBaseUrl("baseUrl");
        service1Config.setPaths(paths);
        
        when(configProperties.getService1()).thenReturn(service1Config);
    }
    
    @Test
    void getMessageOne_whenIsValid() {
        MessageDTO messageOne = getMessage("one");
        getRequestMock();
        when(responseMock.bodyToMono(new ParameterizedTypeReference<MessageDTO>() {
        })).thenReturn(Mono.just(messageOne));

        Mono<MessageDTO> messageOneMono = service.getMessageOne();
        verify(responseMock, times(1)).bodyToMono(new ParameterizedTypeReference<MessageDTO>() {});
        assertThat(messageOneMono.block()).isEqualTo(messageOne);
    }
    
    @Test
    void getMessageTwo_whenIsValid() {
        MessageDTO messageTwo = getMessage("two");
        getRequestMock();
        when(responseMock.bodyToMono(new ParameterizedTypeReference<MessageDTO>() {
        })).thenReturn(Mono.just(messageTwo));

        Mono<MessageDTO> messageOneMono = service.getMessageTwo();
        verify(responseMock, times(1)).bodyToMono(new ParameterizedTypeReference<MessageDTO>() {});
        assertThat(messageOneMono.block()).isEqualTo(messageTwo);
    }
    
    @Test
    void execute_whenIsValid() {
        getRequestMock();
        when(responseMock.bodyToMono(Void.class)).thenReturn(Mono.empty());

        service.execute();
        verify(responseMock, times(1)).bodyToMono(Void.class);
    }

    
    @SuppressWarnings("unchecked")
    private void getRequestMock() {
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(anyString())).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.retrieve()).thenReturn(responseMock);
    }

    private MessageDTO getMessage(String messageId) {
        return MessageDTO.builder()
                .id("id")
                .message(messageId)
                .build();
    }

}
