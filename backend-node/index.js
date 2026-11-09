const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');

const app = express();
app.use(express.json());
app.use(cors());

// Configuración de la conexión base de datos nativa de MySQL
const db = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '2817',
    database: 'tienda_pc'
});

db.connect((err) => {
    if (err) {
        console.error('Error al conectar a la base de datos:', err);
        return;
    }
    console.log('[OK] Conectado exitosamente a la base de datos MySQL');

    // Crear la tabla usuarios automáticamente si no existe en tienda_pc
    const sqlCrearTabla = `
        CREATE TABLE IF NOT EXISTS usuarios (
            id INT AUTO_INCREMENT PRIMARY KEY,
            nombre VARCHAR(100) NOT NULL,
            correo VARCHAR(100) NOT NULL UNIQUE,
            password VARCHAR(255) NOT NULL,
            fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
    `;
    db.query(sqlCrearTabla, (errTabla) => {
        if (errTabla) {
            console.error('[ERROR] No se pudo verificar la tabla usuarios:', errTabla.message);
        } else {
            console.log('[OK] Tabla usuarios verificada/creada correctamente en MySQL');
        }
    });
});

// Ruta de prueba para verificar la API
app.get('/', (req, res) => {
    res.send('API de Tienda PC funcionando al 100%');
});

// Iniciar servidor en el puerto 3000
app.listen(3000, () => {
    console.log('Servidor corriendo en http://localhost:3000');
});

// 1. Obtener lista de productos
app.get('/productos', (req, res) => {
    db.query('SELECT * FROM productos', (err, results) => {
        if (err) return res.status(500).json({ error: 'Error en la base de datos' });
        res.json(results);
    });
});

// 2. Modificar un producto (por ID) desde Postman
app.put('/productos/:id', (req, res) => {
    const { id } = req.params;
    const { nombre, categoria, precio, stock } = req.body;

    const campos = [];
    const valores = [];

    if (nombre !== undefined) { campos.push('nombre = ?'); valores.push(nombre); }
    if (categoria !== undefined) { campos.push('categoria = ?'); valores.push(categoria); }
    if (precio !== undefined) { campos.push('precio = ?'); valores.push(precio); }
    if (stock !== undefined) { campos.push('stock = ?'); valores.push(stock); }

    if (campos.length === 0) {
        return res.status(400).json({ error: 'Debes enviar al menos un campo para actualizar (nombre, precio, stock o categoria)' });
    }

    valores.push(id);
    const sql = `UPDATE productos SET ${campos.join(', ')} WHERE id = ?`;

    db.query(sql, valores, (err, result) => {
        if (err) return res.status(500).json({ error: 'Error al actualizar producto: ' + err.message });
        if (result.affectedRows === 0) return res.status(404).json({ error: 'Producto no encontrado' });
        res.json({ message: 'Producto actualizado con éxito' });
    });
});

// 3. Consultar lista de usuarios registrados
app.get('/usuarios', (req, res) => {
    db.query('SELECT id, nombre, correo, fecha_registro FROM usuarios', (err, results) => {
        if (err) return res.status(500).json({ error: 'Error al consultar usuarios: ' + err.message });
        res.json(results);
    });
});

// 4. Registrar nuevo usuario
app.post(['/registrar', '/registro'], (req, res) => {
    const { nombre, correo, password } = req.body;
    if (!nombre || !correo || !password) {
        return res.status(400).json({ error: 'Todos los campos son obligatorios' });
    }
    db.query('INSERT INTO usuarios (nombre, correo, password) VALUES (?, ?, ?)', [nombre, correo, password], (err, result) => {
        if (err) {
            console.error('Error al registrar usuario:', err);
            if (err.code === 'ER_DUP_ENTRY') {
                return res.status(400).json({ error: 'El correo electrónico ya está registrado' });
            }
            return res.status(500).json({ error: 'Error en la base de datos: ' + err.message });
        }
        res.json({ message: 'Usuario registrado con éxito' });
    });
});

// 5. Iniciar sesión
app.post('/login', (req, res) => {
    const { correo, password } = req.body;
    if (!correo || !password) {
        return res.status(400).json({ error: 'Ingresa correo y contraseña' });
    }
    db.query('SELECT * FROM usuarios WHERE correo = ? AND password = ?', [correo, password], (err, result) => {
        if (err) return res.status(500).json({ error: 'Error al iniciar sesión' });
        if (result.length === 0) return res.status(401).json({ error: 'Usuario o contraseña incorrectos' });
        res.json({ message: 'Inicio de sesión exitoso', usuario: { id: result[0].id, nombre: result[0].nombre, correo: result[0].correo } });
    });
});
