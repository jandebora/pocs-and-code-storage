package com.ccbravo.pocs.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;

@Data
@Builder
public class MessageDTO {

    private String message;
    
    @Default
    private String id = UUID.randomUUID().toString();
    
}
