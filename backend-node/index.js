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
});

// Ruta de prueba para verificar la API
app.get('/', (req, res) => {
    res.send('API de Tienda PC funcionando al 100%');
});

// Iniciar servidor en el puerto 3000
app.listen(3000, () => {
    console.log('Servidor corriendo en http://localhost:3000');
});
app.get('/productos', (req, res) => {
    db.query('SELECT * FROM productos', (err, results) => {
        if (err) return res.status(500).json({ error: 'Error en la base de datos' });
        res.json(results);
    });
});