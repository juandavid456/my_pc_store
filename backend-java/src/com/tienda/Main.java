package com.tienda;

import com.tienda.conexion.ConexionBD;
import com.tienda.dao.ProductoDAO;
import com.tienda.modelo.Producto;

import java.util.List;
import java.util.Scanner;

/**
 * Clase principal ejecutable del proyecto formativo My PC Store.
 * Demuestra la conexión JDBC y el funcionamiento completo del CRUD por consola.
 * Evidencia SENA: GA7-220501096-AA2-EV01.
 */
public class Main {

    private static final ProductoDAO dao = new ProductoDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=======================================================================");
        System.out.println("           MY PC STORE - MÓDULO DE PRODUCTOS (BACKEND JAVA)            ");
        System.out.println("            EVIDENCIA SENA: GA7-220501096-AA2-EV01                     ");
        System.out.println("=======================================================================");

        // 1. Probar la conexión JDBC (Indicador 1)
        boolean conexionOk = ConexionBD.probarConexion();
        if (!conexionOk) {
            System.err.println("⚠️ No fue posible conectar con MySQL.");
            System.err.println("Verifique que XAMPP esté abierto y el módulo MySQL esté en verde (Start).");
            return;
        }

        // Si se pasa argumento "auto", corre la prueba automática para pantallazos
        if (args.length > 0 && args[0].equalsIgnoreCase("auto")) {
            ejecutarDemostracionAutomatica();
            return;
        }

        // Menú principal
        int opcion = -1;
        do {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            try {
                String linea = scanner.nextLine();
                opcion = Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            System.out.println();
            switch (opcion) {
                case 1:
                    listarProductos();
                    break;
                case 2:
                    insertarProducto();
                    break;
                case 3:
                    actualizarProducto();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 5:
                    ejecutarDemostracionAutomatica();
                    break;
                case 0:
                    System.out.println("¡Hasta pronto! Saliendo del sistema...");
                    break;
                default:
                    System.out.println("❌ Opción inválida. Intente de nuevo.");
            }
            System.out.println();
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("                         MENÚ PRINCIPAL CRUD                           ");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println(" 1. [READ]    Listar todos los productos");
        System.out.println(" 2. [CREATE]  Insertar un nuevo producto");
        System.out.println(" 3. [UPDATE]  Actualizar datos de un producto");
        System.out.println(" 4. [DELETE]  Eliminar un producto");
        System.out.println(" 5. [DEMO]    Ejecutar demostración automática del CRUD (para pantallazos)");
        System.out.println(" 0. Salir");
        System.out.println("-----------------------------------------------------------------------");
    }

    // --- OPERACIÓN READ ---
    private static void listarProductos() {
        System.out.println(">>> LISTADO ACTUAL DE PRODUCTOS EN LA BASE DE DATOS (READ):");
        List<Producto> lista = dao.listar();
        if (lista.isEmpty()) {
            System.out.println("No hay productos registrados actualmente.");
        } else {
            System.out.println("-----------------------------------------------------------------------");
            for (Producto p : lista) {
                System.out.println(p);
            }
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("Total de productos: " + lista.size());
        }
    }

    // --- OPERACIÓN CREATE ---
    private static void insertarProducto() {
        System.out.println(">>> REGISTRO DE NUEVO PRODUCTO (CREATE):");
        try {
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine().trim();

            System.out.print("Categoría (ej: PC Gamer, Laptops, Monitores, Periféricos): ");
            String categoria = scanner.nextLine().trim();

            System.out.print("Precio ($ COP): ");
            double precio = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("Stock (cantidad en inventario): ");
            int stock = Integer.parseInt(scanner.nextLine().trim());

            Producto nuevo = new Producto(nombre, categoria, precio, stock);
            boolean exito = dao.crear(nuevo);
            if (exito) {
                System.out.println("✅ ¡Producto insertado con éxito! ID asignado: " + nuevo.getId());
            } else {
                System.out.println("❌ No se pudo insertar el producto.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en los datos ingresados: " + e.getMessage());
        }
    }

    // --- OPERACIÓN UPDATE ---
    private static void actualizarProducto() {
        System.out.println(">>> ACTUALIZACIÓN DE PRODUCTO (UPDATE):");
        try {
            System.out.print("Ingrese el ID del producto a modificar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Producto existente = dao.buscarPorId(id);
            if (existente == null) {
                System.out.println("❌ No existe ningún producto con el ID: " + id);
                return;
            }

            System.out.println("Producto actual: " + existente);
            System.out.print("Nuevo nombre (presione Enter para conservar '" + existente.getNombre() + "'): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) existente.setNombre(nombre);

            System.out.print("Nueva categoría (presione Enter para conservar '" + existente.getCategoria() + "'): ");
            String categoria = scanner.nextLine().trim();
            if (!categoria.isEmpty()) existente.setCategoria(categoria);

            System.out.print("Nuevo precio (presione Enter para conservar '" + existente.getPrecio() + "'): ");
            String precioStr = scanner.nextLine().trim();
            if (!precioStr.isEmpty()) existente.setPrecio(Double.parseDouble(precioStr));

            System.out.print("Nuevo stock (presione Enter para conservar '" + existente.getStock() + "'): ");
            String stockStr = scanner.nextLine().trim();
            if (!stockStr.isEmpty()) existente.setStock(Integer.parseInt(stockStr));

            boolean exito = dao.actualizar(existente);
            if (exito) {
                System.out.println("✅ ¡Producto actualizado con éxito!");
            } else {
                System.out.println("❌ No se pudo actualizar el producto.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error en los datos: " + e.getMessage());
        }
    }

    // --- OPERACIÓN DELETE ---
    private static void eliminarProducto() {
        System.out.println(">>> ELIMINACIÓN DE PRODUCTO (DELETE):");
        try {
            System.out.print("Ingrese el ID del producto que desea eliminar: ");
            int id = Integer.parseInt(scanner.nextLine().trim());

            Producto existente = dao.buscarPorId(id);
            if (existente == null) {
                System.out.println("❌ No existe ningún producto con el ID: " + id);
                return;
            }

            System.out.println("Producto a eliminar: " + existente);
            System.out.print("¿Está seguro de eliminarlo? (S/N): ");
            String resp = scanner.nextLine().trim();
            if (resp.equalsIgnoreCase("S")) {
                boolean exito = dao.eliminar(id);
                if (exito) {
                    System.out.println("✅ ¡Producto eliminado con éxito!");
                } else {
                    System.out.println("❌ No se pudo eliminar el producto.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Ejecuta automáticamente el ciclo completo del CRUD paso por paso.
     * Ideal para tomar los pantallazos que exige la guía del SENA.
     */
    public static void ejecutarDemostracionAutomatica() {
        System.out.println("=======================================================================");
        System.out.println("        INICIANDO DEMOSTRACIÓN AUTOMÁTICA DEL CRUD (EVIDENCIA SENA)    ");
        System.out.println("=======================================================================");

        // 1. Estado inicial de la base de datos (READ)
        System.out.println("\n[PASO 1 - READ INICIAL] Consultando productos existentes en la base de datos:");
        List<Producto> listaInicial = dao.listar();
        for (Producto p : listaInicial) {
            System.out.println("   " + p);
        }

        // 2. Operación CREATE (Insertar)
        System.out.println("\n[PASO 2 - OPERACIÓN CREATE] Insertando un nuevo producto de prueba...");
        Producto nuevo = new Producto("Teclado Mecanico RGB Redragon Kumara", "Perifericos", 185000.0, 20);
        boolean insertado = dao.crear(nuevo);
        if (insertado) {
            System.out.println("   ✅ INSERT exitoso. ID generado por BD: " + nuevo.getId());
            System.out.println("   Fila agregada: " + nuevo);
        }

        // 3. Verificar que aparece en la BD (READ)
        System.out.println("\n[PASO 3 - READ TRAS INSERCIÓN] Verificando que el nuevo producto aparece en la BD:");
        for (Producto p : dao.listar()) {
            System.out.println("   " + p);
        }

        // 4. Operación UPDATE (Actualizar)
        System.out.println("\n[PASO 4 - OPERACIÓN UPDATE] Modificando el producto ID " + nuevo.getId() + "...");
        nuevo.setPrecio(159900.0); // Descuento promocional
        nuevo.setStock(12);       // Venta de unidades
        boolean actualizado = dao.actualizar(nuevo);
        if (actualizado) {
            System.out.println("   ✅ UPDATE exitoso para el ID " + nuevo.getId());
        }

        // 5. Verificar la actualización (READ)
        System.out.println("\n[PASO 5 - READ TRAS ACTUALIZACIÓN] Consultando producto modificado por ID:");
        Producto modificado = dao.buscarPorId(nuevo.getId());
        System.out.println("   Datos actualizados: " + modificado);

        // 6. Operación DELETE (Eliminar)
        System.out.println("\n[PASO 6 - OPERACIÓN DELETE] Eliminando el producto de prueba ID " + nuevo.getId() + "...");
        boolean eliminado = dao.eliminar(nuevo.getId());
        if (eliminado) {
            System.out.println("   ✅ DELETE exitoso para el ID " + nuevo.getId());
        }

        // 7. Estado final de la base de datos (READ)
        System.out.println("\n[PASO 7 - READ FINAL] Verificando estado final de la tabla (el producto ya no debe estar):");
        for (Producto p : dao.listar()) {
            System.out.println("   " + p);
        }

        System.out.println("\n=======================================================================");
        System.out.println("  ✅ ¡DEMOSTRACIÓN FINALIZADA CON ÉXITO! TODAS LAS OPERACIONES CUMPLIDAS");
        System.out.println("=======================================================================");
    }
}
