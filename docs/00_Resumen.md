# Envio — Resumen del microservicio

## Propósito

Administra la asignación del repartidor y el tracking gps del pedido en tiempo real.

## Contexto

- **Caso**: FoodGo (delivery de comida a domicilio)
- **Microservicio**: delivery
- **Base path**: `/api/envios`

## Responsabilidad única (SRP)

Asignación y tracking de repartidores. El servicio atiende un único dominio de negocio y tiene una sola razón de cambio. Entrega su propia base de datos en memoria (H2) y expone su API REST de forma independiente, garantizando **bajo acoplamiento** y **alta cohesión** dentro de la arquitectura de microservicios del caso.

## Requisitos del caso que cubre

RF-05 (asignar repartidor y seguimiento GPS), RNF-05 (rendimiento: latencia GPS < 5s)

## Stack tecnológico

| Componente | Tecnología |
|---|---|
| Framework | Spring Boot 3.3 |
| Lenguaje | Java 21 |
| Build | Maven |
| Persistencia | Spring Data JPA + H2 (in-memory) |
| Validación | Bean Validation (`jakarta.validation`) |
| API/Docs | springdoc-openapi — Swagger UI + OpenAPI yaml + ReDoc |
| Calidad | JaCoCo (cobertura LINE 100%) + Cucumber (BDD REST) |
| Contenedores | Docker + Docker Compose |

## Entradas disponibles desde la web (`/`)

La página raíz presenta el servicio y enlaza Swagger UI, OpenAPI yaml, ReDoc y la consola H2.
