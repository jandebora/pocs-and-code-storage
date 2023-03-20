# Webflux Poc

La idea de está POC es crear dos servicios, uno de ellos que emulará ser el que contiene las peticiones que requerimos llamar desde otro.

* Servicio1: servicio que contiene las peticiones a ejecutar
* Servicio2: servicio que llama al anterior para construir una respuesta

## Servicio1

Como se ha comentado previamente, este servicio contendrá varias peticiones REST preparadas para ser consumidas desde otro.

Contiene las siguientes peticiones:

1. **/v1/messages/one**: devuelve un mensaje con un identificador alfanumérico aleatorio.
1. **/v1/messages/two**: devuelve un mensaje con un identificador alfanumérico aleatorio.
1. **/v1/messages/execute**: devuelve un estado 204 sin body y tarda 5 segundos en ejecutarse.

Para consumir este servicio se ha habilitado la [URL de Swagger](http://localhost:8080/swagger-ui/index.html). En principio no es necesario hacerlo, pues el que tenemos que evaluar es el siguiente servicio.

## Servicio2

Es el encargado de consumir el anterior para construir un mensaje conjunto. La idea es que trás cada petición a los endpoints que devuelven un mensaje se llame al de ejecución que añade un delay de 5s, por lo que si las peticiones son síncronas se tardaría más de 10 segundos.

Contiene las siguientes peticiones:

1. **/v1/messageSums/sync**: petición sin asincronía.
1. **/v1/messageSums/async**: petición con asincronía.

Para consumir este servicio se ha habilitado la [URL de Swagger](http://localhost:8081/swagger-ui/index.html).

## Webflux

La idea de usar webflux es porque permite hacer tanto peticiones síncronas (block) como asíncronas (subscribe). Dentro de la filosofía de la programación reactiva idealmente todas las peticiones deberían ser asíncronas, y el hecho de usar peticiones bloqueantes es ir en contra de la propia definición. No obstante, esta opción existe porque Spring recomienda el uso de webflux para sustituir las clásicas operaciones con RestTemplate. Por lo que puede ser usado para las dos cosas.

### Desarrollando el servicio Webflux

Nuestro servicio que contiene las peticiones a otro es llamado `ServiceConsumer`. En él hacemos una breve configuración inicial (puede hacerse de la forma que se vea mejor, no es obligatorio) a través de `@PostConstruct` donde cargamos la url base del servicio a consumir obtenido directamente a través del `application.yml`.

Este servicio al ser consumido dejaría las peticiones completadas para ser lanzadas de la forma que se requiera (block o subscribe), por lo que el hecho de utilizar un método de está clase no hace que se envíe la petición. Lo veremos próximamente en el servicio que lo consume.

```java
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
```

### Consumiendo el servicio

A continuación habría que crear el servicio que hace uso de estas peticiones. Vamos a crear dos, uno que utiliza asíncronia en los métodos que demoran tiempo y otro que no. Simulando un poco el escenario que se pretende en el contexto real del proyecto.

#### Usando peticiones síncronas (similar a RestTemplate)

Este método sería idéntico al escenario actual, donde tenemos todas las peticiones usando RestTemplate. Al llamar al servicio consumidor y usar el método `.block()` de WebClient, estaríamos creando una petición síncrona con los datos introducidos en el servicio anteriormente creado.

Como podemos observar, tras cada petición que necesitamos de obtención de datos ejecutamos la que añade únicamente delay (y simula ser una operación cuyo resultado no necesitamos para operar). Finalmente se construye un DTO con los datos a mostrar en el controlador.

```java
public MessageSumDTO getMessageSumSync() {
    log.info("Calling synchronous messageOne URI");
    MessageDTO messageOne = service1Consumer.getMessageOne().block();
    
    log.info("Calling synchronous execute URI");
    service1Consumer.execute().block();
    
    log.info("Calling synchronous messageTwo URI");
    MessageDTO messageTwo = service1Consumer.getMessageTwo().block();
    
    log.info("Calling synchronous execute URI");
    service1Consumer.execute().block();
    
    return createSum(messageOne, messageTwo);
}
```

#### Usando peticiones asíncronas

En este caso sustituimos las operaciones de las que no necesitamos respuesta con `.subscribe()` para no esperar a que su ejecución finalice para continuar el flujo. Todo el resto del método es similar al anterior.

```java
public MessageSumDTO getMessageSumAsync() {
    log.info("Calling synchronous messageOne URI");
    MessageDTO messageOne = service1Consumer.getMessageOne().block();
    
    log.info("Calling asynchronous execute URI");
    service1Consumer.execute().subscribe();
    
    log.info("Calling synchronous messageTwo URI");
    MessageDTO messageTwo = service1Consumer.getMessageTwo().block();
    
    log.info("Calling asynchronous execute URI");
    service1Consumer.execute().subscribe();
    
    return createSum(messageOne, messageTwo);
}
```


### Tests unitarios

Al principio el desarrollo de tests unitarios pueden resultar algo complejos, es por ello que se facilita una clase de tests usando Mockito que agilizará este proceso. (*ver tests unitarios dentro del proyecto del* _**servicio2**_).

Esta clase está construida de forma sencilla para verificar pocas condiciones, pero la idea es ser lo más completo posible haciendo tests unitarios.

## Enlaces de interés

1. [Tratamiento de errores con Webflux](https://medium.com/a-developers-odyssey/spring-web-client-exception-handling-cd93cf05b76)
1. [Tests unitarios con Webflux](https://www.baeldung.com/spring-mocking-webclient)
1. [¿Qué ocurre si tenemos spring-web y spring-webflux a la vez?](https://stackoverflow.com/questions/51377675/dont-spring-boot-starter-web-and-spring-boot-starter-webflux-work-together)