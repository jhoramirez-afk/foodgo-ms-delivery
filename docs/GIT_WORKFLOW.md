# Flujo Git de la EP02

main mantiene la entrega estable. develop integra los cambios. feature/ep02-reglas-delivery contiene las reglas, pruebas y ejemplos del dominio.

Los commits se crean por cambios efectivos: modelo/service, pruebas y documentación/Postman. Se conservan fechas reales. Una integración local en develop conserva la rama feature y usa un commit de merge descriptivo. La propuesta develop → main se revisa mediante PR y se fusiona manualmente tras comprobar CI.

No usar force push ni modificar fechas para simular avances.
