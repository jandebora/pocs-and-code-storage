# multiple-swagger-ui-poc

# Explicaciones previas

Swagger es una interfaz única que te permite administrar una serie de especificaciones que normalmente están cacheadas en memoria una vez arrancas la propia interfaz. Tiene distintas opciones configurables bien explicadas en su sitio web. Para hacer esta prueba tenemos que conseguir que a través de una única interfaz podamos tener una especificación para cada fila de la tabla y podamos luego generar una URL de Swagger UI única para ella, de forma que podrá administrar el CRUD de esa tabla concreta.

## Configuración de swagger en YML

Se han añadido tres campos adicionales.

* `queryConfigEnabled: true`: especifica que podemos configurar swagger a través de su URL
* `configUrl: <url>`: especifica donde está la url de configuración de swagger, especificada al por defecto
* `url: <url>`: especifica la url de la documentación de swagger que cargará en primera instancia. Apuntamos a la que tiene todos los grupos.

```yml
springdoc:
  swagger-ui:
    oauth:
      tokenURL: url
      client-id: clientId
      client-secret: clientSecret
    display-request-duration: true
    disable-swagger-default-url: true
    tags-sorter: alpha
    operations-sorter: alpha
    queryConfigEnabled: true
    configUrl: /v3/api-docs/swagger-config
    url: /v3/api-docs
```

## Configuración de swagger en Java Config

Extendiendo la configuración a la que estamos acostumbrados, añadimos en esta breve PoC dos grupos de OpenAPI para diferenciar los endpoints generales y los de detalle (este ultimo es el que tendrá que funcionar posteriormente como múltiple).

```java
@Bean
public GroupedOpenApi detailOpenApi() {
    String[] paths = {"/v1/entities/{id}"};
    return GroupedOpenApi.builder().group("detail").pathsToMatch(paths)
            .build();
}

@Bean
public GroupedOpenApi genericOpenApi() {
    String[] paths = {"/v1/entities"};
    return GroupedOpenApi.builder().group("generic").pathsToMatch(paths)
            .build();
}
```

## Creación de un componente para obtener el OpenAPI JSON de un grupo concreto

Necesario para pasos posteriores. Si nos fijamos en este caso hemos puesto a fuego el grupo `"detail"`.

```java
@Component
public class DetailGroupOpenApiResource extends OpenApiResource {


    public DetailGroupOpenApiResource(ObjectFactory<OpenAPIService> openAPIBuilderObjectFactory, AbstractRequestService requestBuilder,
            GenericResponseService responseBuilder, OperationService operationParser,
            Optional<List<OperationCustomizer>> operationCustomizers,
            Optional<List<OpenApiCustomiser>> openApiCustomisers,
            Optional<List<RouterOperationCustomizer>> routerOperationCustomizers,
            Optional<List<OpenApiMethodFilter>> methodFilters,
            SpringDocConfigProperties springDocConfigProperties,
            SpringDocProviders springDocProviders) {
        super("detail", openAPIBuilderObjectFactory, requestBuilder, responseBuilder, operationParser, operationCustomizers, openApiCustomisers, routerOperationCustomizers, methodFilters, springDocConfigProperties, springDocProviders);
    }


    @Override
    protected String getServerUrl(HttpServletRequest request, String apiDocsUrl) {
        return "";
    }

    public String getOpenApiJson() throws JsonProcessingException {
        return writeJsonValue(getOpenApi(Locale.getDefault()));
    }
}
```

## Creación de un servicio 

La idea es generar un servicio que al pasarnos un ID de la tabla nos genere una especificación de OpenAPI para esa fila concreta, y que posteriormente con la configuración por URL de swagger ui podamos hacer uso de esta especificación.

**NOTA:** En este servicio estamos dando por supuesto que la variable que tendrá que ser sustituida por el ID de la tabla es: `{id}`.

```java
@Service
@Slf4j
public class CustomOpenAPIDocFromDatabaseIdServiceImpl implements CustomOpenAPIDocFromDatabaseIdService {

    @Autowired
    private DetailGroupOpenApiResource resource;
    
    @Autowired
    private EntityRepository repository;

    @Override
    @SneakyThrows
    public String getDocumentation(String id) {
        List<String> ids = repository.findAll().stream().map(Entity::getId).toList();
        
        if (!ids.contains(id)) 
            throw new NotFoundException("No se ha encontrado el identificador.");

        try {
            Gson gson = new Gson();
            OpenAPI openAPI = gson.fromJson(resource.getOpenApiJson(), OpenAPI.class);
            Map<String, PathItem> map = openAPI.getPaths();
            Collection<PathItem> pathItems = map.values();
            
            pathItems.forEach(pathItem ->  
                pathItem.readOperations().forEach(operation -> {
                    List<Parameter> parameters = operation.getParameters().stream()
                            .filter(parameter -> parameter.getName().equals("id")).toList();
                    operation.getParameters().removeAll(parameters);
                }));
            
            return Pattern.compile("{id}", Pattern.LITERAL).matcher(gson.toJson(openAPI)).replaceAll(id);
        } catch (JsonProcessingException e) {
            log.error("Ha ocurrido un error");
            return "";
        }
    } 
}
```
Posteriormente se crea un controlador para este servicio.

# Funcionamiento

Una vez desplegamos la PoC:

1. Accedemos a la [URL](http://localhost:8080/swagger-ui.html), lo cual nos lleva a la vista de swagger con esta URL (completada diferente al añadir que es configurable por parámetros).
    * URL de Swagger al cargar la anterior: `http://localhost:8080/swagger-ui/index.html?displayRequestDuration=true&operationsSorter=alpha&tagsSorter=alpha&configUrl=/v3/api-docs/swagger-config`.
2. Creamos una fila a través del POST.
3. Obtenemos el identificador consultandolo a través del listado o detalle.
    * En el body del GET podemos ver la URL que necesitamos para obtener al especificación de swagger concreta.
4. Rellenamos la URL previa del paso 1 diciendole a swagger a través de sus parámetros que cargue la especificación de una URL concreta.
    * Añadimos: `&http://localhost:8080/v1/customOpenAPIDocs/<identificador_generado>`.
    * Quedando la URL concreta de la siguiente forma: `http://localhost:8080/swagger-ui/index.html?displayRequestDuration=true&operationsSorter=alpha&tagsSorter=alpha&configUrl=/v3/api-docs/swagger-config&url=http://localhost:8080/v1/customOpenAPIDocs/<identificador_generado>`
5. Et Violà