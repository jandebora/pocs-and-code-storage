package com.ccbravo.pocs.domain;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Data;

@Document
@Data
public class MongoEntity {

    @Id
    @Schema(accessMode = AccessMode.READ_ONLY)
    private String id;
    
    @NotEmpty
    private String message;
}
