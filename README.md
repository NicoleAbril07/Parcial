# ParkLibre

Aplicación de escritorio Java Swing para administrar un parqueadero de carros, motos y bicicletas.

## Requisitos
- JDK 17 o superior.
- Maven.
- XAMPP con MySQL/MariaDB.

## Base de datos
1. Inicia MySQL desde XAMPP.
2. Abre `http://localhost/phpmyadmin`.
3. Importa o ejecuta `database/parklibre.sql`.

## Configuración
Por defecto la conexión usa:
- URL: `jdbc:mariadb://localhost:3306/parklibre`
- Usuario: `root`
- Contraseña: vacía

Si tu instalación tiene otra contraseña, cambia `DB_USER` y `DB_PASSWORD` en `persistencia/Conexion.java`.

## Ejecutar
Desde la carpeta que contiene `pom.xml`:

```bash
mvn clean compile
mvn exec:java
```

## Funcionalidades
- Registrar entrada de carro, moto o bicicleta.
- Registrar salida y calcular el cobro.
- Marcar lavado para carros y motos.
- Consultar, editar y eliminar registros.
- Consultar vehículos actualmente dentro.
- Consultar recaudo por tipo y fecha.
- Persistencia JDBC con PreparedStatement.
