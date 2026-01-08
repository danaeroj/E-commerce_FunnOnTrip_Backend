-- =====================================================
-- Script de inserción de datos de prueba
-- Proyecto: FunOnTrip / PixelNomads
-- =====================================================

USE funontrip_db;

-- =====================================================
-- 1) Usuarios (tabla padre)
-- =====================================================
INSERT INTO Usuarios (nombre, correo_electronico, contraseña, rol, fecha_registro) VALUES
('Angie Torres', 'anguietorres.92@gmail.com', '$2b$10$Fun2024AngieHash', 'admin', '2025-12-01 10:00:00'),
('Danae Rojas', 'danaero25@gmail.com', '$2b$10$Fun2024DanaeHash', 'admin', '2025-12-02 11:30:00'),
('David Carranco', 'david_carranco1111@outlook.es', '$2b$10$Fun2024DavidHash', 'admin', '2025-12-03 09:15:00'),
('Jared Barranco', 'barrancojared577@gmail.com', '$2b$10$Fun2024JaredHash', 'admin', '2025-12-04 18:45:00'),
('Fernando Ortiz', 'jorfernandofo@gmail.com', '$2b$10$Fun2024FerHash', 'admin', '2025-12-05 14:20:00');

-- =====================================================
-- 2) Productos
-- =====================================================
INSERT INTO Producto (nombre, descripcion, precio, ubicacion, tipo, imagen_url, stock, activo, fecha_creacion) VALUES
('Escapada Romántica París', 'Viaje romántico de 5 días en París', 25999.00, 'internacional', 'romantico', 'paris.webp', 10, TRUE, NOW()),
('Aventura en Barrancas del Cobre', 'Experiencia extrema en la sierra', 18999.00, 'nacional', 'aventura', 'barrancas.webp', 8, TRUE, NOW()),
('Viaje Familiar Cancún', 'Paquete todo incluido para familia', 22999.00, 'nacional', 'familiar', 'cancun.webp', 12, TRUE, NOW()),
('Ruta Gastronómica Roma', 'Tour culinario por Italia', 27999.00, 'internacional', 'gastronomico', 'roma.webp', 6, TRUE, NOW()),
('Relax en Santorini', 'Vacaciones de descanso frente al mar', 31999.00, 'internacional', 'relax', 'santorini.webp', 5, TRUE, NOW());

-- =====================================================
-- 3) Carritos
-- =====================================================
INSERT INTO Carrito (Usuarios_idUsuarios, estado, fecha_creacion, fecha_actualizacion) VALUES
(1, 'activo', NOW(), NOW()),
(2, 'activo', NOW(), NOW()),
(3, 'activo', NOW(), NOW()),
(4, 'abandonado', NOW(), NOW()),
(5, 'convertido', NOW(), NOW());

-- =====================================================
-- 4) Detalle_carrito
-- =====================================================
INSERT INTO Detalle_carrito (Carrito_idCarrito, Producto_idProducto, cantidad, subtotal, fecha_agregado) VALUES
(1, 1, 1, 25999.00, NOW()),
(1, 2, 1, 18999.00, NOW()),
(2, 3, 2, 45998.00, NOW()),
(3, 4, 1, 27999.00, NOW()),
(5, 5, 1, 31999.00, NOW());

-- =====================================================
-- 5) Pedidos
-- =====================================================
INSERT INTO Pedidos (Usuarios_idUsuarios, subtotal, impuestos, total, estado, metodo_pago, fecha_pedido, fecha_actualizacion) VALUES
(5, 31999.00, 5120.00, 37119.00, 'pagado', 'tarjeta', NOW(), NOW()),
(3, 27999.00, 4480.00, 32479.00, 'procesando', 'paypal', NOW(), NOW()),
(2, 45998.00, 7360.00, 53358.00, 'pendiente', 'tarjeta', NOW(), NOW()),
(1, 25999.00, 4160.00, 30159.00, 'completado', 'transferencia', NOW(), NOW()),
(4, 18999.00, 3040.00, 22039.00, 'cancelado', 'tarjeta', NOW(), NOW());

-- =====================================================
-- 6) Detalle_pedidos
-- =====================================================
INSERT INTO Detalle_pedidos (Pedidos_idPedidos, Producto_idProducto, cantidad, precio_unitario, subtotal) VALUES
(1, 5, 1, 31999.00, 31999.00),
(2, 4, 1, 27999.00, 27999.00),
(3, 3, 2, 22999.00, 45998.00),
(4, 1, 1, 25999.00, 25999.00),
(5, 2, 1, 18999.00, 18999.00);

-- =====================================================
-- 7) Contacto
-- =====================================================
INSERT INTO Contacto (nombre, correo_electronico, telefono, asunto, mensaje, atendido, fecha_envio) VALUES
('Cliente Uno', 'cliente1@mail.com', '5551234567', 'Información', 'Quiero más detalles del viaje a París', FALSE, NOW()),
('Cliente Dos', 'cliente2@mail.com', '5559876543', 'Reserva', 'Deseo reservar Cancún', TRUE, NOW()),
('Cliente Tres', 'cliente3@mail.com', NULL, 'Promociones', '¿Hay descuentos activos?', FALSE, NOW()),
('Cliente Cuatro', 'cliente4@mail.com', '5551112233', 'Pago', 'Problema con mi tarjeta', TRUE, NOW()),
('Cliente Cinco', 'cliente5@mail.com', '5553334444', 'Soporte', 'No puedo iniciar sesión', FALSE, NOW());
