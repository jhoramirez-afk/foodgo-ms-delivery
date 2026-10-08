# Historial y revisión EP02

Los cambios se realizaron con commits reales, sin modificar fechas ni el historial de EP01.

1. `feature/jpa-relations`: entidades relacionadas, FK y carga de relaciones JPA.
2. `feature/crud-errors`: endpoints relacionados, transacciones y errores HTTP JSON.
3. `feature/persistence-tests-docs`: persistencia H2 en disco, perfil MySQL, pruebas, Postman y documentación.
4. Cada rama se integra en `develop` mediante un merge que conserva su trazabilidad.
5. El PR `develop` → `main` reúne la actualización para que el propietario pueda revisar y validar antes de fusionar.

Para probar la propuesta: `git fetch origin`, `git switch develop`, `mvn clean install` y ejecutar el JAR indicado en README. Para comprobar el historial: `git log --graph --oneline --all`.

El PR no se fusiona automáticamente. Las comprobaciones de GitHub Actions ejecutan Maven y JaCoCo; la revisión humana y la demostración local se realizan antes de integrar en main.
