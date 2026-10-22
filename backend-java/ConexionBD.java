import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Parametros de conexion a MySQL en XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/tienda_pc";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    // Metodo para conectar con la base de datos
    public static Connection conectar() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo conectar a la base de datos: " + e.getMessage());
        }
        return con;
    }
}
