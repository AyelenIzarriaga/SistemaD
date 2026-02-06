📦 Sistema de Caja con Telegram Bot

Proyecto backend en Java + Spring Boot que permite gestionar una caja diaria mediante un bot de Telegram.
El sistema registra ingresos y gastos, los asocia a proveedores y calcula resúmenes por día y por mes.

La idea principal es que el usuario pueda manejar su caja directamente desde Telegram sin necesidad de un frontend tradicional.

🚀 Funcionalidades actuales

✅ Registro de ingresos.

✅ Registro de gastos.

✅ Asociación automática a proveedores.

✅ Creación automática de proveedores si no existen.

✅ Registro por fecha de movimiento (no solo hoy).

✅ Cálculo de:

Entradas.

Salidas.

Libre (entradas - salidas).

✅ Resumen diario.

✅ Resumen mensual.

✅ Persistencia en base de datos con JPA / Hibernate.

✅ Integración con Telegram Bot API.

🧠 Lógica del sistema

Cada movimiento guarda:

Tipo (ENTRADA / SALIDA)

Fecha de movimiento

Fecha de registro

Monto

Proveedor

Origen

Almacén

Si el proveedor no existe:

Se crea automáticamente.

El resumen calcula:

Entradas = suma de ENTRADA

Salidas = suma de SALIDA

Libre = Entradas - Salidas

🛠️ Tecnologías usadas

Java

Spring Boot

Spring Data JPA

Hibernate

PostgreSQL / MySQL (configurable)

Telegram Bots API

Maven

🧩 Estado del proyecto

Actualmente el proyecto está en desarrollo.

Próximos pasos planeados:

🔹 Soporte multiusuario real por chatId.

🔹 Comandos con fecha manual.

🔹 Historial por rangos.

🔹 Reportes exportables.

🔹 Control de sesiones.

🔹 Roles de usuario.

🔹 Mejor parsing de mensajes.
