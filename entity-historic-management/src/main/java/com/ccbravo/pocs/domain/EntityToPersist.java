package com.ccbravo.pocs.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Entity
@Audited
@EntityListeners(AuditingEntityListener.class)
public class EntityToPersist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "order_seq_gen")
    @SequenceGenerator(name = "order_seq_gen", sequenceName ="order_id_seq", allocationSize = 1)
    @Schema(accessMode = AccessMode.READ_ONLY)
    private Long id;
    
    private String name;
    
    @CreatedDate
    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Schema(accessMode = AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

}
