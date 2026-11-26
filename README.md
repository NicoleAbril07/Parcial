# ParkLibre

**Universidad Libre – Facultad de Ingeniería**  
**Asignatura:** Programación 1  
**Parcial – Tercer corte**

## Información del proyecto

- **Estudiante:** [Nicole Camila Abril Reyes]
- **Variante:** 1 – Parqueadero
- **Nombre de la aplicación:** ParkLibre
- **Patrón de diseño:** Factory Method
- **Lenguaje:** Java
- **Interfaz gráfica:** Java Swing
- **Base de datos:** MariaDB
- **Acceso a datos:** JDBC

## Descripción

ParkLibre es una aplicación de escritorio para la gestión de un parqueadero. Permite registrar vehículos, consultar los vehículos registrados, editar y eliminar registros, registrar salidas y calcular las tarifas correspondientes según el tipo de vehículo y el tiempo de permanencia.

El sistema maneja tres tipos de vehículos:

- Carros.
- Motocicletas.
- Bicicletas.

También contempla el servicio de lavado para carros y motocicletas.

## Tecnologías utilizadas

- Java 17 o superior.
- Maven (para la construcción del proyecto).
- MariaDB.
- JDBC.
- Java Swing.

## Estructura del proyecto

```text
ParkLibre/
├── database/
│   └── parklibre.sql
├── src/
│   └── main/
│       └── java/
│           └── co/edu/unilibre/parklibre/
│               ├── excepciones/
│               ├── modelo/
│               ├── patron/
│               ├── persistencia/
│               ├── servicio/
│               ├── vista/
│               └── Main.java
├── pom.xml
└── README.md
```

## Configuración de la base de datos

1. Instalar o iniciar MariaDB. También se puede utilizar XAMPP.
2. Iniciar el servicio MySQL desde el panel de XAMPP.
3. Abrir phpMyAdmin.
4. Importar el archivo `database/parklibre.sql`.

El script crea la base de datos `parklibre` y la tabla necesaria para almacenar los registros del parqueadero.

### Configuración de conexión

La conexión predeterminada utiliza:

- **Servidor:** localhost
- **Puerto:** 3306
- **Base de datos:** parklibre
- **Usuario:** root
- **Contraseña:** vacía

Si las credenciales de MariaDB son diferentes, deben actualizarse en la clase `persistencia/Conexion.java`.

## Ejecución de la aplicación

### Opción 1. Ejecutar mediante el archivo JAR (recomendada)

No es necesario tener Maven ni Visual Studio Code instalados.

Requisitos:
- Java 17 o superior.
- MariaDB iniciado.
- Base de datos `parklibre` creada.

Ejecutar desde una terminal ubicada en la carpeta donde se encuentra el archivo:

```bash
java -jar ParkLibre.jar
```

También se puede intentar abrir el archivo `ParkLibre.jar` con doble clic, siempre que Windows tenga Java configurado para abrir archivos JAR.

### Opción 2. Ejecutar desde el código fuente con Maven

Clonar el repositorio:

```bash
git clone URL_DEL_REPOSITORIO
```

Entrar en la carpeta:

```bash
cd ParkLibre
```

Compilar el proyecto:

```bash
mvn clean compile
```

Ejecutar la aplicación:

```bash
mvn exec:java
```

Para generar el archivo JAR:

```bash
mvn clean package
```

El archivo generado se encuentra en:

```text
target/ParkLibre.jar
```

## Patrón de diseño utilizado

Se implementa el patrón creacional **Factory Method**, que permite delegar la creación de los diferentes tipos de vehículos a creadores concretos.

Se utiliza tanto durante el registro de vehículos como durante la reconstrucción de objetos recuperados de la base de datos.

## Base de datos

El proyecto utiliza MariaDB mediante JDBC para almacenar y recuperar la información de los vehículos y sus registros de estacionamiento.

---

**ParkLibre – Universidad Libre**  
Programación 1 – Parcial tercer corte
