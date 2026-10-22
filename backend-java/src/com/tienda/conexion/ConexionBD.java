package com.tienda.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL mediante JDBC.
 * Cumple con el Indicador 1 de la evidencia SENA GA7-220501096-AA2-EV01.
 */
public class ConexionBD {

    // Parámetros de configuración de la conexión
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_pc?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; // En XAMPP la contraseña por defecto es vacía

    /**
     * Obtiene una nueva conexión a la base de datos.
     * @return Objeto Connection si fue exitoso, o null en caso de error.
     */
    public static Connection conectar() {
        Connection conexion = null;
        try {
            // Cargar el driver JDBC de MySQL
            Class.forName(DRIVER);
            // Establecer la conexión con el motor MySQL
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ ERROR: No se encontró el driver JDBC de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ ERROR al conectar con la base de datos MySQL:");
            System.err.println("   Mensaje: " + e.getMessage());
            System.err.println("   Código de error: " + e.getErrorCode());
        }
        return conexion;
    }

    /**
     * Método para probar rápidamente la conexión (útil para la evidencia y pantallazos).
     */
    public static boolean probarConexion() {
        System.out.println("=============================================================");
        System.out.println("  INDICADOR 1 SENA: PRUEBA DE CONEXIÓN CON JDBC A MYSQL");
        System.out.println("=============================================================");
        System.out.println("Intentando conectar a: " + URL);
        System.out.println("Usuario: " + USUARIO);

        try (Connection con = conectar()) {
            if (con != null && !con.isClosed()) {
                System.out.println("✅ ¡CONEXIÓN EXITOSA! Objeto Connection establecido correctamente.");
                System.out.println("   Base de datos: " + con.getCatalog());
                System.out.println("   Driver: " + con.getMetaData().getDriverName() + " " + con.getMetaData().getDriverVersion());
                System.out.println("=============================================================\n");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Falló la prueba de conexión: " + e.getMessage());
        }
        return false;
    }
}
