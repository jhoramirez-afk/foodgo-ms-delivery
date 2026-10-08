# Checklist EP02 — foodgo-ms-delivery

| Indicador | Evidencia en el repositorio |
|---|---|
| IE1 | Controladores RESTful para CRUD de Envio y EventoTracking; HTTP 201/204/400/404/409 |
| IE2 | Paquetes `controller`, `service`, `repository`, `model`, inyección por constructor |
| IE6 | `Envio` con `@OneToMany` y `EventoTracking` con `@ManyToOne` |
| IE8 | `pom.xml` con Web, JPA, Validation, H2, MySQL, OpenAPI y plugins Maven |
| IE9 | Flujo previsto `main` -> `develop` -> `feature/*`; commits descriptivos |
| IE3 | Colección Postman incluida para casos exitosos y de error |
| IE4 | `application-h2.yml`, `application-mysql.yml` y `docker-compose.yml` |
| IE5 | CRUD persistente sobre entidades JPA |
| IE7 | README documenta `mvn clean`, `test`, `install`, `package` y ejecución del `.jar` |
| IE10 | README documenta clonación, dependencias, ejecución H2 y ejecución MySQL |
