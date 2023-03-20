# generic-oauth-poc

# Pre-condiciones
* Se requiere que el Identity Provider siga el estánda OpenID y tenga los siguientes endpoints:
    * Verificar el identificador (issuer) emisor
    * Verificar la firma del token (jwk-uri) a través de la clave pública

Se han realizado pruebas con Keycloak (estándar de la unidad) y Authentik (desplegable en local con el `docker-compose` provisto)

# Desarrollo de la PoC

## Configuración de seguridad

Se ha desarrollado la clase de seguridad `SecurityOAuthConfiguration` tal y como se hace normalmente en los proyectos de la unidad. La diferencia, a parte de hacerlo usando los nuevos estándares de Spring Boot 3, es que hay que añadir lo siguiente a la clase que configura `HttpSecurity`:

```java
    ...
    .and()
    .oauth2ResourceServer().jwt();
```

**NOTA**: A modo de prueba, también hemos añadido un firewall para ir normalizándolo en los próximos desarrollos. _Ver método_ _`webSecurityCustomizer`_

## Configuración por YAML

Tal y como indica la configuración, hay que añadir las URIs de identificación del emisor del token (`issuer-uri`) y compartición de clave pública de la firma (`jwk-set-uri`).

```yaml
spring:
  security:
    oauth2:
      resourceServer:
        jwt:
          issuer-uri: # insert issuer uri
          jwk-set-uri: # Insert cert uri
# Example
#
# Keycloak
#          issuer-uri: https://localhost:8090/auth/realms/<realmName>
#          jwk-set-uri: https://localhost:8090/auth/realms/<realmName>/protocol/openid-connect/certs
# Authentik
#          issuer-uri: http://localhost:8090/application/o/<appName>/
#          jwt-set-uri: http://localhost:8090/application/o/<appName>/jwks/
```

## Creación de una clase para manejo de roles

En vista de que esta comprobación solamente comprueba la veracidad del token, y que ha sido firmado por el Identity Provider indicado, hay que dar un paso más allá para verificar los roles asignados a ese token.

Dado que esta formación de los roles es dependiente del proveedor concreto, es necesario especificar que estructura dentro del `payload` del JWT que identifica al o a los roles.

En primer lugar debemos definir por tanto una clase de configuración para obtener esta estructura a través de nuestro yml:

```java
@Configuration
@ConfigurationProperties(prefix = "architecture.security.token.role")
@Data
public class TokenRoleProperties {
    private Map<String, Object> structure;
}
```

Está definido como un `Map` porque la idea es que sea genérico.

En nuestro `application.yml` deberiamos indicar que estructura tiene ese JSON que llega a especificar los roles.

```yaml
architecture:
  security:
    token:
      role:
        enabled: false
        structure:
          # Insert Role JWT JSON Object Structure

# This structure is the JWT JSON Object which contains the roles
# The example below indicates a structure like {...,"resource_access":{"login":{ "roles": ['role1','role2']}}}
#          resource_access:
#            login:
#              roles: list
#
# At the moment, at the end of the structure you must select the type of object which contains roles
# It should be 'list' (see example above) or 'value' in case there was one role only
# Other types will throw an error
```

Posteriormente, a través de esa estructura habría que crear un componente de spring capaz de verificar si ese JWT tiene los roles asignados.

Esto se hace en la clase `TokenProvider` dentro del paquete `security`. En esta PoC se ha elaborado pensando que podría haber dos tipos de formas de identificar los roles: a través de una lista o a través de un simple valor (un JWT == un rol). Puede extenderse a gusto.

Dentro de este componente se han creado dos métodos típicos que solemos usar con Spring Security:
* `boolean hasAnyRole(String... roles)`
* `boolean hasRole(String role)`

## Uso del TokenProvider y obtención de datos del token

```java
@RestController
@PreAuthorize("isAuthenticated()")
public class MyController {

    @GetMapping(value = "/helloWorld")
    @PreAuthorize("@tokenProvider.hasAnyRole('randomRole','admin')")
    public String helloWorld(@Parameter(hidden = true) @AuthenticationPrincipal Jwt principal){
        return "Hello World " + principal.getClaim("preferred_username");
    }
}
```

Como podemos observar, con la anotación `@PreAuthorize` podemos usar el componente inyectado en nuestro contexto de spring e invocar al método concreto. También podemos ver como a través del controlador podemos recibir y tratar el JWT que llega de la petición con la anotación `@AuthenticationPrincipal`.
