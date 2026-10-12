# Docker EP02: delivery con PostgreSQL

El build utiliza Java 21 y Spring Boot, ejecuta Maven clean verify y empaqueta el JAR. Compose inicia una base PostgreSQL del dominio y la app con perfil postgres. El control de la base ejecuta SELECT 1; el de la app consulta una ruta REST que accede a la BD.

## Ejecutar desde un clon nuevo

```cmd
git clone https://github.com/jhoramirez-afk/foodgo-ms-delivery.git
cd foodgo-ms-delivery
copy .env.example .env
docker compose config --quiet
docker compose build --progress plain
docker compose up -d --wait --wait-timeout 300
docker compose ps
```

Antes de ejecutar Compose, completar DB_PASSWORD en .env. Este archivo está excluido de Git y del contexto Docker. La contraseña del contenedor puede elegirse para esa instancia independiente.

La API se publica por defecto en http://127.0.0.1:18085/api/delivery. APP_PORT permite cambiarlo. Dentro de Compose, Java conecta a postgres:5432; pgAdmin desde Windows conecta a 127.0.0.1:54325, base foodgo_delivery, usuario DB_USER. DB_EXPOSE_PORT permite cambiar ese puerto.

PostgreSQL 18 conserva sus datos en el volumen postgres_data montado en /var/lib/postgresql. El nombre del proyecto Compose es foodgo-delivery-postgres: los contenedores y volúmenes MySQL anteriores se conservan separados. No copiar archivos físicos MySQL al volumen PostgreSQL.

## CRUD y persistencia

Importar las colecciones de postman/ y ajustar baseUrl al APP_PORT. Ejecutar crear, consultar y actualizar; comprobar los mismos ID en pgAdmin. Mostrar errores y completar DELETE.

```cmd
docker compose restart
docker compose ps
docker compose logs --tail 50 app
docker compose stop
docker compose start --wait --wait-timeout 300
```

Repetir GET y SELECT con los mismos ID tras el reinicio. No añadir -v/--volumes a down durante la demostración: esos parámetros eliminan los volúmenes de esta instancia.

Para registrar la app en Eureka del host, definir SPRING_PROFILES_ACTIVE=postgres,eureka y EUREKA_URL=http://host.docker.internal:8761/eureka/ en .env. Los puertos de Postman corresponden a los publicados en Windows.

Referencias: [PostgreSQL oficial](https://hub.docker.com/_/postgres), [healthchecks y orden](https://docs.docker.com/compose/how-tos/startup-order/).
