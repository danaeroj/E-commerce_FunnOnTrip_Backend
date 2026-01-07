-- DB/create.sql
-- Script de creación de base de datos y tablas
-- Proyecto: FunOnTrip / PixelNomads

DROP DATABASE IF EXISTS funontrip_db;
CREATE DATABASE funontrip_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE funontrip_db;

-- Asegura consistencia al correr el script
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Tabla: Usuarios
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Usuarios (
  idUsuarios INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL COMMENT 'Nombre completo del usuario',
  correo_electronico VARCHAR(100) NOT NULL COMMENT 'Email único del usuario',
  contraseña VARCHAR(255) NOT NULL COMMENT 'Password hasheado (bcrypt)',
  telefono VARCHAR(20) NULL DEFAULT NULL COMMENT 'Teléfono del usuario',
  rol ENUM('user','admin') NOT NULL DEFAULT 'user' COMMENT 'Tipo de usuario',
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación de cuenta',
  PRIMARY KEY (idUsuarios),
  UNIQUE KEY correo_electronico_UNIQUE (correo_electronico),
  KEY idx_rol (rol)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Almacena información de usuarios del sistema';


-- -----------------------------------------------------
-- Tabla: Producto
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Producto (
  idProducto INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(200) NOT NULL COMMENT 'Nombre del paquete turístico',
  descripcion TEXT NOT NULL COMMENT 'Descripción detallada del paquete',
  precio DECIMAL(10,2) NOT NULL COMMENT 'Precio en MXN',
  ubicacion ENUM('nacional','internacional','petfriendly') NOT NULL COMMENT 'Categoría de ubicación',
  tipo ENUM('romantico','aventura','familiar','gastronomico','relax') NOT NULL COMMENT 'Tipo de experiencia',
  imagen_url VARCHAR(255) NULL DEFAULT NULL COMMENT 'Ruta de la imagen del producto',
  stock INT NOT NULL DEFAULT 0 COMMENT 'Disponibilidad del paquete',
  activo TINYINT NOT NULL DEFAULT TRUE COMMENT 'Estado del producto (activo/inactivo)',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del producto',
  PRIMARY KEY (idProducto),
  KEY idx_ubicacion (ubicacion),
  KEY idx_tipo (tipo),
  KEY idx_activo (activo)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Catálogo de paquetes turísticos';

-- -----------------------------------------------------
-- Tabla: Carrito
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Carrito (
  idCarrito INT NOT NULL AUTO_INCREMENT,
  Usuarios_idUsuarios INT NOT NULL COMMENT 'Usuario dueño del carrito',
  estado ENUM('activo','abandonado','convertido') NOT NULL DEFAULT 'activo' COMMENT 'Estado del carrito',
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última modificación',
  PRIMARY KEY (idCarrito),
  KEY fk_Carrito_Usuarios_idx (Usuarios_idUsuarios),
  KEY idx_estado (estado),
  CONSTRAINT fk_Carrito_Usuarios
    FOREIGN KEY (Usuarios_idUsuarios)
    REFERENCES Usuarios (idUsuarios)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Carritos de compra de usuarios';

-- -----------------------------------------------------
-- Tabla: Detalle_carrito
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Detalle_carrito (
  idDetalle_carrito INT NOT NULL AUTO_INCREMENT,
  Carrito_idCarrito INT NOT NULL COMMENT 'Carrito al que pertenece',
  Producto_idProducto INT NOT NULL COMMENT 'Producto agregado',
  cantidad INT NOT NULL DEFAULT 1 COMMENT 'Cantidad de productos',
  subtotal DECIMAL(10,2) NOT NULL COMMENT 'Precio × Cantidad',
  fecha_agregado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Cuándo se agregó',
  PRIMARY KEY (idDetalle_carrito),
  KEY fk_Detalle_carrito_Carrito_idx (Carrito_idCarrito),
  KEY fk_Detalle_carrito_Producto_idx (Producto_idProducto),
  CONSTRAINT fk_Detalle_carrito_Carrito
    FOREIGN KEY (Carrito_idCarrito)
    REFERENCES Carrito (idCarrito)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_Detalle_carrito_Producto
    FOREIGN KEY (Producto_idProducto)
    REFERENCES Producto (idProducto)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Items individuales en cada carrito';

-- -----------------------------------------------------
-- Tabla: Pedidos
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Pedidos (
  idPedidos INT NOT NULL AUTO_INCREMENT,
  Usuarios_idUsuarios INT NOT NULL COMMENT 'Usuario que realizó el pedido',
  subtotal DECIMAL(10,2) NOT NULL COMMENT 'Total antes de impuestos',
  impuestos DECIMAL(10,2) NOT NULL COMMENT 'IVA (16%)',
  total DECIMAL(10,2) NOT NULL COMMENT 'Total final',
  estado ENUM('pendiente','pagado','procesando','enviado','completado','cancelado') NOT NULL DEFAULT 'pendiente' COMMENT 'Estado del pedido',
  metodo_pago VARCHAR(50) NULL DEFAULT NULL COMMENT 'Forma de pago utilizada',
  fecha_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación',
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última actualización',
  PRIMARY KEY (idPedidos),
  KEY fk_Pedidos_Usuarios_idx (Usuarios_idUsuarios),
  KEY idx_estado (estado),
  KEY idx_fecha_pedido (fecha_pedido),
  CONSTRAINT fk_Pedidos_Usuarios
    FOREIGN KEY (Usuarios_idUsuarios)
    REFERENCES Usuarios (idUsuarios)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Pedidos confirmados de usuarios';

-- -----------------------------------------------------
-- Tabla: Detalle_pedidos
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Detalle_pedidos (
  idDetalle_pedidos INT NOT NULL AUTO_INCREMENT,
  Pedidos_idPedidos INT NOT NULL COMMENT 'Pedido al que pertenece',
  Producto_idProducto INT NOT NULL COMMENT 'Producto comprado',
  cantidad INT NOT NULL COMMENT 'Cantidad comprada',
  precio_unitario DECIMAL(10,2) NOT NULL COMMENT 'Precio al momento de la compra',
  subtotal DECIMAL(10,2) NOT NULL COMMENT 'Precio × Cantidad',
  PRIMARY KEY (idDetalle_pedidos),
  KEY fk_Detalle_pedidos_Pedidos_idx (Pedidos_idPedidos),
  KEY fk_Detalle_pedidos_Producto_idx (Producto_idProducto),
  CONSTRAINT fk_Detalle_pedidos_Pedidos
    FOREIGN KEY (Pedidos_idPedidos)
    REFERENCES Pedidos (idPedidos)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT fk_Detalle_pedidos_Producto
    FOREIGN KEY (Producto_idProducto)
    REFERENCES Producto (idProducto)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Detalle de productos en cada pedido';

-- -----------------------------------------------------
-- Tabla: Contacto
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Contacto (
  idContacto INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL COMMENT 'Nombre del remitente',
  correo_electronico VARCHAR(100) NOT NULL COMMENT 'Email de contacto',
  telefono VARCHAR(20) NULL DEFAULT NULL COMMENT 'Teléfono opcional',
  asunto VARCHAR(200) NOT NULL COMMENT 'Tema del mensaje',
  mensaje TEXT NOT NULL COMMENT 'Contenido del mensaje',
  atendido TINYINT NOT NULL DEFAULT FALSE COMMENT 'Estado de atención',
  fecha_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha del mensaje',
  PRIMARY KEY (idContacto),
  KEY idx_atendido (atendido),
  KEY idx_fecha_envio (fecha_envio)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Mensajes del formulario de contacto';

-- Restaurar configuración original
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
