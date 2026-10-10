# API REST: delivery

Base local: http://localhost:8085/api. Swagger UI: http://localhost:8085/swagger-ui/index.html.

| Método | Ruta | HTTP de éxito |
|---|---|---:|
| POST | /envios | 201 |
| GET | /envios | 200 |
| GET | /envios/{id} | 200 |
| PUT | /envios/{id} | 200 |
| DELETE | /envios/{id} | 204 |
| POST | /envios/{id}/tracking | 201 |
| GET | /envios/{id}/tracking | 200 |
| GET | /tracking/{id} | 200 |
| PUT | /tracking/{id} | 200 |
| DELETE | /tracking/{id} | 204 |

## Crear entidad principal

```json
{
  "pedido": "PED-DEMO",
  "repartidor": "Diego Herrera",
  "estado": "ASIGNADO"
}
```

## Crear entidad relacionada

```json
{
  "estado": "ASIGNADO",
  "latitud": -33.43121,
  "longitud": -70.61975,
  "fechaHora": "2026-10-01T13:30:00"
}
```

Usar el ID retornado por la creación del padre. Los ID son generados por la BD. Editar los hijos mediante sus propias rutas. Ver las reglas y los campos calculados en REGLAS_EP02.md.

Errores: 400 para datos o JSON inválidos; 404 para recurso/relación local inexistente; 409 para conflictos de integridad o unicidad cuando corresponda. Un campo demasiado largo devuelve 400. Los mensajes y validationErrors se entregan mediante ApiExceptionHandler.
