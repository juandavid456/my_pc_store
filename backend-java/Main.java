import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("   CONECTANDO A MYSQL (XAMPP) CON JDBC...        ");
        System.out.println("=================================================");

        // Paso 1: Probar la conexion
        Connection con = ConexionBD.conectar();
        if (con != null) {
            System.out.println("[OK] Conexion exitosa con la base de datos!\n");
        } else {
            System.out.println("[ERROR] No se pudo conectar. Verifica que MySQL en XAMPP este en verde.");
            return;
        }

        ProductoDAO dao = new ProductoDAO();
        int opcion = -1;

        // Menú que espera a que tú decidas qué hacer paso por paso
        while (opcion != 0) {
            System.out.println("---------------------------------------------");
            System.out.println("            MENU DE CONTROL CRUD             ");
            System.out.println("---------------------------------------------");
            System.out.println("1. [READ]    Ver todos los productos guardados");
            System.out.println("2. [CREATE]  Guardar un producto nuevo a mano");
            System.out.println("3. [UPDATE]  Cambiar el precio a un producto");
            System.out.println("4. [DELETE]  Borrar un producto");
            System.out.println("0. Salir");
            System.out.print("Elige una opcion (0-4): ");

            try {
                opcion = Integer.parseInt(teclado.nextLine().trim());
            } catch (Exception e) {
                opcion = -1;
            }

            System.out.println();

            switch (opcion) {
                case 1:
                    // OPERACION READ: Consultar la base de datos
                    System.out.println(">>> CONSULTANDO LA BASE DE DATOS (READ)...");
                    dao.listar();
                    break;

                case 2:
                    // OPERACION CREATE: Escribir un producto nuevo
                    System.out.println(">>> GUARDAR NUEVO PRODUCTO (CREATE)");
                    System.out.print("Escribe el nombre del producto: ");
                    String nombre = teclado.nextLine();

                    System.out.print("Escribe la categoria (ej: Perifericos, Monitores): ");
                    String categoria = teclado.nextLine();

                    System.out.print("Escribe el precio (ej: 85000): ");
                    double precio = Double.parseDouble(teclado.nextLine());

                    System.out.print("Escribe el stock (cuantos tienes en bodega): ");
                    int stock = Integer.parseInt(teclado.nextLine());

                    dao.insertar(nombre, categoria, precio, stock);
                    break;

                case 3:
                    // OPERACION UPDATE: Modificar el precio
                    System.out.println(">>> ACTUALIZAR PRECIO (UPDATE)");
                    System.out.print("Escribe el ID del producto que quieres modificar: ");
                    int idModificar = Integer.parseInt(teclado.nextLine());

                    System.out.print("Escribe el NUEVO precio: ");
                    double nuevoPrecio = Double.parseDouble(teclado.nextLine());

                    dao.actualizarPrecio(idModificar, nuevoPrecio);
                    break;

                case 4:
                    // OPERACION DELETE: Borrar un producto
                    System.out.println(">>> ELIMINAR PRODUCTO (DELETE)");
                    System.out.print("Escribe el ID del producto que quieres borrar: ");
                    int idBorrar = Integer.parseInt(teclado.nextLine());

                    dao.eliminar(idBorrar);
                    break;

                case 0:
                    System.out.println("Saliendo del programa. Adios!");
                    break;

                default:
                    System.out.println("Opcion no valida. Escribe un numero del 0 al 4.");
            }

            System.out.println();
        }
    }
}
