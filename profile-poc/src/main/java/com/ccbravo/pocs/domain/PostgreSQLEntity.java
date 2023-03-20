package com.ccbravo.pocs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Data;

@Data
@Entity
public class PostgreSQLEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName ="order_id_seq", allocationSize = 1)
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;
    
    @NotEmpty
    private String message;

}
