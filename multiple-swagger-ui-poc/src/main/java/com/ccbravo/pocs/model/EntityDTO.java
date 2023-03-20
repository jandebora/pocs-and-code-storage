package com.ccbravo.pocs.model;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntityDTO {

    @Schema(accessMode = AccessMode.READ_ONLY)
    private String id;
    
    @NotEmpty
    private String name;
    
    @Schema(accessMode = AccessMode.READ_ONLY)
    private String uri;
}
