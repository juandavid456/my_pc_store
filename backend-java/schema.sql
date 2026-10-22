-- Script de creación de la Base de Datos para el proyecto My PC Store
-- Evidencia SENA: GA7-220501096-AA2-EV01

CREATE DATABASE IF NOT EXISTS tienda_pc CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tienda_pc;

-- Tabla de productos del módulo de inventario / catálogo
CREATE TABLE IF NOT EXISTS productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    precio DOUBLE NOT NULL,
    stock INT NOT NULL
);

-- Datos iniciales de prueba (coherentes con el frontend de My PC Store)
INSERT INTO productos (nombre, categoria, precio, stock) VALUES
('Ventilador XPG Hurricane 140mm ARGB Blanco', 'Refrigeracion', 69000.0, 15),
('PC Gamer Ryzen 5 5600G 16GB SSD 500GB', 'PC Gamer', 2150000.0, 5),
('Monitor LG UltraGear 27" 144Hz', 'Monitores', 890000.0, 8);
