# Pruebas de delivery

- Pruebas unitarias de service y controller.
- MockMvc + JPA/H2: CRUD de ambas entidades, relación, actualización y cascada.
- Casos de error y reglas específicas del dominio: ver CrudIntegrationTest.
- Cucumber: listado y ciclo completo del CRUD por HTTP.

```bash
mvn clean install
mvn package
```

El pom mantiene la comprobación de cobertura mínima de 80% de líneas. Revisar target/site/jacoco/index.html y target/surefire-reports. No confundir el umbral configurado con el porcentaje obtenido.

Con MySQL: seguir README, importar postman/FoodGo-delivery-EP02.postman_collection.json y ejecutar las carpetas en orden. Las peticiones de la última carpeta eliminan los registros de esa ejecución. El escenario conjunto utiliza datos ficticios coherentes y conserva el pedido para observarlo en Workbench si se omite la limpieza opcional.
