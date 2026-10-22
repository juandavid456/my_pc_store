import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductoDAO {

    // 1. CREATE (Insertar un producto)
    public void insertar(String nombre, String categoria, double precio, int stock) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, categoria);
            ps.setDouble(3, precio);
            ps.setInt(4, stock);
            ps.executeUpdate();
            System.out.println("[OK - CREATE] Producto guardado con exito: " + nombre);

        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo insertar: " + e.getMessage());
        }
    }

    // 2. READ (Consultar y mostrar todos los productos)
    public void listar() {
        String sql = "SELECT * FROM productos";
        System.out.println("\n--- LISTA DE PRODUCTOS EN LA BASE DE DATOS ---");
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   " | " + rs.getString("nombre") +
                                   " | Categoria: " + rs.getString("categoria") +
                                   " | Precio: $" + rs.getDouble("precio") +
                                   " | Stock: " + rs.getInt("stock"));
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo listar: " + e.getMessage());
        }
        System.out.println("----------------------------------------------\n");
    }

    // 3. UPDATE (Actualizar precio)
    public void actualizarPrecio(int id, double nuevoPrecio) {
        String sql = "UPDATE productos SET precio = ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, nuevoPrecio);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("[OK - UPDATE] Precio actualizado para el producto ID: " + id);

        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo actualizar: " + e.getMessage());
        }
    }

    // 4. DELETE (Eliminar producto)
    public void eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("[OK - DELETE] Producto eliminado con ID: " + id);

        } catch (SQLException e) {
            System.out.println("[ERROR] No se pudo eliminar: " + e.getMessage());
        }
    }
}
