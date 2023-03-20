# entity-historic-management

Creación de tablas historicas automáticas usando Hibernate Envers

# Resumen

Siguiendo el [artículo de Baeldung](https://www.baeldung.com/database-auditing-jpa) tenemos una guía completa de auditoría con JPA.

# Pasos a seguir

## Añadir dependencia de Hibernate Envers

```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-envers</artifactId>
</dependency>
```

## Habilitar auditoría en clase de configuración

```java
@SpringBootApplication
@EnableJpaAuditing
public class EntityHistoricManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EntityHistoricManagementApplication.class, args);
	}

}
```

## Añadir anotaciones a entidad

Hay que añadir las anotaciones `@Audited` para poder auditar en la tabla de replica y `@EntityListeners(AuditingEntityListener.class)` para poder auditar los campos anotados de creación y actualización. Todo a nivel de clase.

```java
@Data
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

```

**IMPORTANTE:** En el caso de que haya campos que no queramos auditar (como relaciones que hayamos creado con JPA), es importante añadir la anotación `@NotAudited` en ese atributo concreto.

## Añadir configuración de Envers

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/postgresdb
    username: user
    password: password
    hikari:
      connection-timeout: 20000
      maximum-pool-size: 5
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      org:
        hibernate:
          envers:
            audit_table_suffix: _AUDIT
            revision_field_name: REVISION_ID
            revision_type_field_name: REVISION_TYPE
            store_data_at_delete: true
```

Si nos fijamos, estamos añadiendo unas propiedades de envers donde especificamos:

* El sufijo que tendrá la tabla de auditoría
* El campo de revisión que identificará que actualización sobre la fila es (númerico)
* El nombre del campo de tipo de revisión, que significa:
    * 0 - ADD
    * 1 - MOD
    * 2 - DEL
* Especificación de si queremos guardar al borrar.