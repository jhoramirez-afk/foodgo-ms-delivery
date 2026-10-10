# Ejecución local EP02

Seguir README para clonar, instalar dependencias y ejecutar con Java 21 y Maven. El perfil test usa H2 en memoria para aislar las pruebas. El perfil local usa H2 en disco; el perfil mysql usa el servidor relacional elegido.

Para la demostración con Workbench: usar los mismos DB_HOST, DB_PORT, DB_NAME y DB_USER que en Spring Boot, y entregar DB_PASSWORD mediante el entorno. Workbench consulta la BD; no sustituye al servidor MySQL. En este equipo, FoodGo usa 127.0.0.1:3307. En otro equipo, adaptar el host, puerto y usuario sin modificar el código.

```bash
mvn clean install
mvn package
java -jar target/delivery-svc-2.0.0.jar --spring.profiles.active=mysql
```

No usar credenciales reales en archivos versionados. Los ejemplos y el escenario se cargan desde Postman. La evaluación se demuestra localmente y no requiere componentes adicionales de infraestructura.
