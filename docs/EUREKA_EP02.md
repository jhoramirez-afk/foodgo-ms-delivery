# Registro Eureka: delivery

El perfil eureka registra este servicio con su nombre spring.application.name y puerto. El perfil mysql mantiene la persistencia del dominio. Eureka recibe registros y heartbeats; las rutas REST y reglas de negocio se conservan. El registro no agrega llamadas automáticas entre las API.

## Arranque directo en PowerShell / terminal de VS Code

Iniciar primero foodgo-eureka-server en 8761 y MySQL local en 3307. En una terminal situada donde está pom.xml:

```powershell
java -version
mvn -version
mvn clean
mvn install
mvn package
$env:DB_HOST = '127.0.0.1'
$env:DB_PORT = '3307'
$env:DB_NAME = 'foodgo_delivery'
$env:DB_USER = 'foodgo_ep02'
$claveFoodGo = Read-Host 'Contraseña MySQL' -AsSecureString
$env:DB_PASSWORD = [Net.NetworkCredential]::new('', $claveFoodGo).Password
$env:EUREKA_URL = 'http://127.0.0.1:8761/eureka/'
java -Xms64m -Xmx384m -jar target/delivery-svc-2.0.0.jar --spring.profiles.active=mysql,eureka --server.port=8085
```

Esperar Started ...Application y verificar http://127.0.0.1:8085/swagger-ui/index.html. En http://127.0.0.1:8761 aparecen los registros de los ocho servicios; puede tardar unos segundos. Postman comprueba el CRUD y los errores. Workbench consulta el mismo host/puerto/esquema de las variables DB_.

La contraseña se introduce personalmente y no debe guardarse en README/Git ni mostrarse en el video. Ctrl+C cierra este JAR normalmente y permite mostrar la baja de la instancia en Eureka.

## Docker opcional

Para registrar la app Docker en un Eureka del host, establecer SPRING_PROFILES_ACTIVE=mysql,eureka y EUREKA_URL=http://host.docker.internal:8761/eureka/ en .env. Dentro de Docker, la instancia anuncia su IP interna; los puertos publicados de Windows siguen controlados por APP_PORT. La colección usada en Postman debe coincidir con esos puertos.

## Pruebas y compatibilidad

Las pruebas existentes usan el perfil test; eureka.client.enabled queda false fuera del perfil eureka para no depender de un registro externo durante Maven. Spring Cloud 2023.0.6 se utiliza con el Spring Boot 3.3.5 y Java 21 existentes.
Referencias: [compatibilidad](https://github.com/spring-cloud/spring-cloud-release/wiki/Supported-Versions), [cliente Eureka](https://docs.spring.io/spring-cloud-netflix/docs/4.1.6/reference/html/).
