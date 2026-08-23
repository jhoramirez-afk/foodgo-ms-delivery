# language: es
Característica: Servicio Envio (microservicio delivery del caso FoodGo)
  Los escenarios validan el contrato REST del microservicio alineado a sus endpoints.

  Escenario: el listado del recurso responde 200
    Dado el servicio "Envio" está disponible
    Cuando consulto el listado de "envios"
    Entonces el listado responde con código 200

  Escenario: ciclo de vida completo del recurso
    Dado un nuevo "envio" con pedido "hola-cucumber"
    Cuando consulto el "envio" recién creado
    Entonces el recurso tiene pedido "hola-cucumber" y código 200
    Cuando actualizo el "envio" con pedido "cucumber-actualizado"
    Entonces el recurso queda con pedido "cucumber-actualizado" y código 200
    Cuando elimino el "envio"
    Entonces la eliminación responde con código 204
    Y al consultar el "envio" eliminado responde 404
