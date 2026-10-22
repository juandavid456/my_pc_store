package com.tienda.dao;

import com.tienda.conexion.ConexionBD;
import com.tienda.modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Producto.
 * Implementa las 4 operaciones del CRUD (Create, Read, Update, Delete) con JDBC.
 * Cumple con el Indicador 2 y 4 de la evidencia SENA.
 */
public class ProductoDAO {

    /**
     * OPERACIÓN C (CREATE): Inserta un nuevo producto en la base de datos.
     * @param producto Objeto con los datos a insertar.
     * @return true si se insertó con éxito, false en caso contrario.
     */
    public boolean crear(Producto producto) {
        String sql = "INSERT INTO productos (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getCategoria());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                // Obtenemos el ID autoincremental generado por MySQL
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al insertar producto: " + e.getMessage());
        }
        return false;
    }

    /**
     * OPERACIÓN R (READ): Consulta todos los productos almacenados en la base de datos.
     * @return Lista de objetos Producto.
     */
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, categoria, precio, stock FROM productos ORDER BY id ASC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * OPERACIÓN R (READ por ID): Busca un producto específico por su clave primaria.
     * @param id Identificador único del producto.
     * @return Objeto Producto encontrado o null si no existe.
     */
    public Producto buscarPorId(int id) {
        String sql = "SELECT id, nombre, categoria, precio, stock FROM productos WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("categoria"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar producto por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * OPERACIÓN U (UPDATE): Actualiza los datos de un producto existente.
     * @param producto Objeto con los nuevos valores y el ID a modificar.
     * @return true si se actualizó, false en caso contrario.
     */
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getCategoria());
            ps.setDouble(3, producto.getPrecio());
            ps.setInt(4, producto.getStock());
            ps.setInt(5, producto.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar producto: " + e.getMessage());
        }
        return false;
    }

    /**
     * OPERACIÓN D (DELETE): Elimina un producto de la base de datos por su ID.
     * @param id Identificador del producto a eliminar.
     * @return true si se eliminó, false en caso contrario.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar producto: " + e.getMessage());
        }
        return false;
    }
}
