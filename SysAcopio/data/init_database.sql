-- ============================================
-- Script de Inicialización de SysAcopio
-- Base de Datos: SQLite
-- ============================================

-- NOTA: Ejecutar este script DESPUÉS de que Hibernate cree las tablas automáticamente
-- Hibernate crea las tablas al iniciar la aplicación por primera vez

-- ============================================
-- INSERTAR USUARIOS DE PRUEBA
-- ============================================

INSERT INTO usuarios (username, password, nombre_completo, rol, activo, fecha_creacion, fecha_actualizacion)
VALUES
    ('admin', 'admin123', 'Administrador del Sistema', 'ADMIN', 1, datetime('now'), datetime('now')),
    ('operador', 'oper123', 'Juan Carlos Pérez López', 'OPERADOR', 1, datetime('now'), datetime('now')),
    ('supervisor', 'super123', 'María Elena Torres Ruiz', 'SUPERVISOR', 1, datetime('now'), datetime('now'));

-- ============================================
-- INSERTAR MATERIALES (MINERALES)
-- ============================================

INSERT INTO materiales (nombre, codigo, descripcion, unidad_medida, precio_referencia, activo, fecha_creacion, fecha_actualizacion)
VALUES
    ('ORO', 'AU', 'Oro en bruto - Alta pureza', 'GRAMOS', 2050.00, 1, datetime('now'), datetime('now')),
    ('MATE', 'MT', 'Mate de oro - Concentrado', 'GRAMOS', 1950.00, 1, datetime('now'), datetime('now')),
    ('PLATA', 'AG', 'Plata en bruto', 'GRAMOS', 24.50, 1, datetime('now'), datetime('now')),
    ('COBRE', 'CU', 'Cobre refinado', 'KILOGRAMOS', 8.75, 1, datetime('now'), datetime('now'));

-- ============================================
-- INSERTAR PROVEEDORES DE PRUEBA
-- ============================================

-- Proveedor 1: Persona Natural
INSERT INTO proveedores (tipo_documento, numero_documento, nombres, apellidos, razon_social,
                        direccion, telefono, email, banco, numero_cuenta, activo,
                        fecha_creacion, fecha_actualizacion)
VALUES
    ('DNI', '12345678', 'María Rosa', 'González Torres', NULL,
     'Jr. Los Mineros 456, Puno', '951234567', 'maria.gonzalez@email.com',
     'Banco de la Nación', '0011-2233-4455-6677', 1,
     datetime('now'), datetime('now'));

-- Proveedor 2: Persona Natural
INSERT INTO proveedores (tipo_documento, numero_documento, nombres, apellidos, razon_social,
                        direccion, telefono, email, banco, numero_cuenta, activo,
                        fecha_creacion, fecha_actualizacion)
VALUES
    ('DNI', '87654321', 'Juan Carlos', 'Mamani Quispe', NULL,
     'Av. Floral 789, Juliaca', '965432109', 'juan.mamani@email.com',
     'BCP', '1234-5678-9012-3456', 1,
     datetime('now'), datetime('now'));

-- Proveedor 3: Empresa (RUC)
INSERT INTO proveedores (tipo_documento, numero_documento, nombres, apellidos, razon_social,
                        direccion, telefono, email, banco, numero_cuenta, activo,
                        fecha_creacion, fecha_actualizacion)
VALUES
    ('RUC', '20123456789', NULL, NULL, 'MINERA SUR ANDINA S.A.C.',
     'Av. Industrial 1234, Puno', '051-365478', 'contacto@minerasur.com.pe',
     'Interbank', '9876-5432-1098-7654', 1,
     datetime('now'), datetime('now'));

-- Proveedor 4: Persona Natural
INSERT INTO proveedores (tipo_documento, numero_documento, nombres, apellidos, razon_social,
                        direccion, telefono, email, banco, numero_cuenta, activo,
                        fecha_creacion, fecha_actualizacion)
VALUES
    ('DNI', '45678912', 'Ana Lucía', 'Ccama Huanca', NULL,
     'Jr. Libertad 321, Azángaro', '987654321', 'ana.ccama@email.com',
     'Banco de la Nación', '5544-3322-1100-9988', 1,
     datetime('now'), datetime('now'));

-- Proveedor 5: Empresa (RUC)
INSERT INTO proveedores (tipo_documento, numero_documento, nombres, apellidos, razon_social,
                        direccion, telefono, email, banco, numero_cuenta, activo,
                        fecha_creacion, fecha_actualizacion)
VALUES
    ('RUC', '20987654321', NULL, NULL, 'COOPERATIVA MINERA AURÍFERA LTDA',
     'Calle Real 567, Puno', '051-351234', 'administracion@coopminera.pe',
     'BBVA', '1111-2222-3333-4444', 1,
     datetime('now'), datetime('now'));

-- ============================================
-- INSERTAR UN ACOPIO DE EJEMPLO (OPCIONAL)
-- ============================================

-- Acopio de ejemplo (Cabecera)
INSERT INTO acopios (numero_acopio, proveedor_id, fecha, subtotal, igv, total,
                     estado, observaciones, usuario_id, fecha_creacion, fecha_actualizacion)
VALUES
    ('ACO-2024-001', 1, date('now'), 5850.00, 0.00, 5850.00,
     'PAGADO', 'Acopio de prueba - Primer registro del sistema', 1,
     datetime('now'), datetime('now'));

-- Detalle del acopio de ejemplo
INSERT INTO acopio_detalles (acopio_id, material_id, peso, ley, deduccion, precio_onza_base,
                            tipo_cambio_dolar, precio_gramo_dolares, precio_gramo_soles,
                            total_pagar)
VALUES
    (1, 1, 150.00, 85.50, 0.10, 2050.00, 3.75, 50.32, 188.70, 28305.00);

-- ============================================
-- INSERTAR HISTORIAL DE AUDITORÍA
-- ============================================

INSERT INTO historial_movimientos (usuario_id, accion, entidad, detalle, fecha)
VALUES
    (1, 'CREAR', 'USUARIO', 'Usuario admin creado en el sistema', datetime('now')),
    (1, 'CREAR', 'MATERIAL', 'Material ORO registrado con código AU', datetime('now')),
    (1, 'CREAR', 'PROVEEDOR', 'Proveedor María Rosa González Torres registrado', datetime('now')),
    (1, 'CREAR', 'ACOPIO', 'Acopio ACO-2024-001 creado por $5,850.00', datetime('now'));

-- ============================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- ============================================

-- Contar registros insertados
SELECT 'Usuarios insertados: ' || COUNT(*) FROM usuarios;
SELECT 'Materiales insertados: ' || COUNT(*) FROM materiales;
SELECT 'Proveedores insertados: ' || COUNT(*) FROM proveedores;
SELECT 'Acopios insertados: ' || COUNT(*) FROM acopios;
SELECT 'Historial insertado: ' || COUNT(*) FROM historial_movimientos;

-- ============================================
-- CONSULTAS DE VERIFICACIÓN
-- ============================================

-- Ver todos los usuarios
SELECT username, nombre_completo, rol, activo FROM usuarios;

-- Ver todos los materiales
SELECT codigo, nombre, precio_referencia, unidad_medida FROM materiales;

-- Ver todos los proveedores
SELECT tipo_documento, numero_documento,
       COALESCE(nombres || ' ' || apellidos, razon_social) as nombre_completo,
       telefono FROM proveedores;

-- Ver acopios con proveedor
SELECT a.numero_acopio,
       COALESCE(p.nombres || ' ' || p.apellidos, p.razon_social) as proveedor,
       a.fecha, a.total, a.estado
FROM acopios a
INNER JOIN proveedores p ON a.proveedor_id = p.id;

-- ============================================
-- FIN DEL SCRIPT
-- ============================================