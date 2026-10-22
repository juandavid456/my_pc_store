package com.tienda.modelo;

/**
 * Clase que representa la entidad Producto en el sistema My PC Store.
 * Cumple con el estándar de codificación Java (JavaBeans / POO).
 */
public class Producto {

    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;

    // Constructor vacío
    public Producto() {
    }

    // Constructor sin ID (usado para insertar nuevos productos antes de tener ID en BD)
    public Producto(String nombre, String categoria, double precio, int stock) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    // Constructor completo con ID (usado al consultar desde la BD)
    public Producto(int id, String nombre, String categoria, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-35s | Cat: %-14s | Precio: $ %,10.2f | Stock: %d uds",
                id, nombre, categoria, precio, stock);
    }
}
