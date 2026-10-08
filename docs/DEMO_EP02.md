# Demostración EP02: delivery

La colección Postman se ejecuta en orden con Collection Runner. Guarda los IDs devueltos en `parentId` y `childId`; no presupone que sean 1. Incluye los diez endpoints, actualización de datos y errores 400, 404 y 409. Los últimos pasos eliminan los datos de demostración y comprueban la cascada.

## Persistencia relacional en H2

1. Ejecutar `mvn clean install` y luego `java -jar target/delivery-svc-2.0.0.jar`.
2. Importar la colección `postman/FoodGo-delivery-EP02.postman_collection.json`.
3. Ejecutar pasos 01 a 08. Abrir `http://localhost:8085/h2-console`.
4. JDBC: `jdbc:h2:file:./data/foodgo_delivery`; usuario `sa`; contraseña vacía.
5. Ejecutar las consultas SQL de abajo. Mostrar la PK de la entidad principal y la FK de la relacionada.
6. Detener con Ctrl+C y volver a ejecutar el mismo JAR desde la misma carpeta. Consultar los mismos IDs para mostrar que los datos continúan en disco.
7. Ejecutar los pasos de error y eliminación (09 a 19). Consultar de nuevo las tablas para mostrar la eliminación.

```sql
SELECT * FROM envios;
SELECT * FROM eventos_tracking;
```

## Guion compartido para un video de 6 a 7 minutos

- 0:00–0:35: ambos estudiantes presentan FoodGo, los ocho servicios y el alcance de EP02.
- 0:35–1:20: mostrar clonación, README, Java 21 y comandos `mvn clean`, `mvn install`, `mvn package`; abrir `target/` y ejecutar el JAR.
- 1:20–2:15: explicar controller → service → repository → model; mostrar `@OneToMany`, `@ManyToOne`, FK y transacciones.
- 2:15–4:30: ejecutar CRUD en Postman, incluidos relacionados y errores; contrastar datos con las consultas SQL. Mostrar resultados del Runner en los otros servicios.
- 4:30–5:30: explicar `application-h2.yml` y `pom.xml`, mostrar persistencia tras reiniciar. El perfil MySQL es opcional y necesita MySQL o Docker previamente disponible.
- 5:30–6:30: mostrar ramas `main`, `develop`, `feature/*`, commits reales y resultados de pruebas Maven; cerrar con lo desarrollado.

Grabar MP4 con las voces de ambos estudiantes, audio claro y duración de 3 a 8 minutos. El guion prepara la evidencia; la grabación y entrega las realizan los estudiantes.

## Límites de la implementación

Los campos de referencia a otros servicios conservan el contrato de EP01. Las relaciones JPA son internas al dominio; no se conectan tablas de servicios diferentes ni se simula una integración entre servicios. Los recursos relacionados se actualizan mediante sus endpoints propios. H2 es una base relacional persistente para la demostración local. La configuración MySQL se entrega para ejecución opcional; su validación debe indicarse según el entorno disponible.
