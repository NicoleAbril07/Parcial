CREATE DATABASE IF NOT EXISTS parklibre
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE parklibre;

CREATE TABLE IF NOT EXISTS estacionamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identificador VARCHAR(10) NOT NULL,
    propietario VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    ingreso DATETIME NOT NULL,
    salida DATETIME NULL,
    lavado BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_identificador_activo (identificador, salida),
    INDEX idx_salida (salida)
);
