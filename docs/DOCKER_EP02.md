# Docker EP02: delivery

Docker construye el mismo código Java 21/Spring Boot del repositorio. El build ejecuta Maven clean verify, pruebas y cobertura; solo el JAR y JRE quedan en la imagen final.
El contenedor app se conecta a mysql:3306 dentro de la red de Compose, usando el perfil mysql. MySQL guarda datos en un volumen independiente. La aplicación espera a que SELECT 1 con la cuenta de aplicación pase; su healthcheck consulta una ruta real contra la BD.

## Ejecutar desde un clon nuevo

```powershell
git clone https://github.com/jhoramirez-afk/foodgo-ms-delivery.git
Set-Location foodgo-ms-delivery
Copy-Item .env.example .env
```

Editar .env y completar DB_PASSWORD y MYSQL_ROOT_PASSWORD con contraseñas locales. .env está excluido de Git y del contexto Docker. APP_PORT es el puerto publicado en Windows; PORT dentro del contenedor no cambia.

```powershell
docker compose config --quiet
docker compose build --progress plain
docker compose up -d --wait --wait-timeout 300
docker compose ps
```

API por defecto: http://127.0.0.1:8085/api/envios. Swagger: http://127.0.0.1:8085/swagger-ui/index.html.
Si el servicio local ya ocupa 8085, usar APP_PORT=18085 en .env. Para Workbench: host 127.0.0.1, puerto DB_EXPOSE_PORT (por defecto 33065), usuario DB_USER y esquema foodgo_delivery.

## CRUD y persistencia

Importar la colección de postman/ y ajustar baseUrl al APP_PORT elegido. Ejecutar crear/leer/actualizar, consultar las tablas en Workbench y después probar errores y DELETE. Los datos son ficticios. Los ID se capturan automáticamente.

```powershell
docker compose restart
docker compose ps
docker compose logs --tail 50 app
docker compose stop
docker compose start --wait --wait-timeout 300
```

Repetir GET por los mismos ID y SELECT para demostrar que los datos persisten. stop/start conserva contenedores y volúmenes. down conserva los volúmenes por defecto; no añadir --volumes/-v durante la demostración porque eliminaría la base.
No copiar los archivos de MySQL de Windows dentro del contenedor. La instancia Docker tiene su propia base; crear sus ejemplos con Postman. La instancia FoodGo local en 3307 se conserva.

## Pauta

La EP02 se evidencia mostrando REST/capas/JPA, conexión en application-mysql.yml y POM, CRUD y errores en Postman/Workbench, Maven/JAR, README y ramas/commits. Docker empaqueta esos mismos componentes; no agrega microservicios ni funciones de negocio.

Referencias: [build por etapas](https://docs.docker.com/build/building/multi-stage/), [orden y healthchecks de Compose](https://docs.docker.com/compose/how-tos/startup-order/), [MySQL oficial](https://hub.docker.com/_/mysql).
