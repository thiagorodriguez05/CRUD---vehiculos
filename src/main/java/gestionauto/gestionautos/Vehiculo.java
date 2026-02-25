package gestionauto.gestionautos;

import java.io.Serializable;

public abstract class Vehiculo
        implements Comparable<Vehiculo>, Serializable {

    private static final long serialVersionUID = 1L;

    protected String patente;
    protected String marca;
    protected int anio;
    protected double precioBase;

    public Vehiculo(String patente, String marca, int anio, double precioBase) {

        validarPatente(patente);
        validarMarca(marca);
        validarAnio(anio);
        validarPrecio(precioBase);

        this.patente = patente.toUpperCase();
        this.marca = marca;
        this.anio = anio;
        this.precioBase = precioBase;
    }

    // ================= VALIDACIONES =================

    private void validarPatente(String patente) {

        if (patente == null || !patente.matches("^[A-Za-z]{3}\\d{3}$")) {
            throw new IllegalArgumentException(
                    "La patente debe tener 3 letras y 3 números (Ej: ABC123)");
        }
    }

    private void validarMarca(String marca) {

        if (marca == null || !marca.matches("^[A-Za-z ]+$")) {
            throw new IllegalArgumentException(
                    "La marca debe contener solo letras");
        }
    }

    private void validarAnio(int anio) {

        if (anio < 1900 || anio > 2026) {
            throw new IllegalArgumentException(
                    "El año debe estar entre 1900 y 2026");
        }
    }

    private void validarPrecio(double precio) {

        if (precio < 1000 || precio > 100000) {
            throw new IllegalArgumentException(
                    "El precio debe estar entre 10000 y 100000");
        }
    }

    // ================= GETTERS =================

    public String getPatente() { return patente; }
    public String getMarca() { return marca; }
    public int getAnio() { return anio; }
    public double getPrecioBase() { return precioBase; }

    public double getPrecioFinal() {
        return calcularPrecio();
    }

    // ================= EQUALS / HASHCODE =================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return patente.equalsIgnoreCase(vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return patente.toUpperCase().hashCode();
    }

    // ================= ORDEN NATURAL =================

    @Override
    public int compareTo(Vehiculo otro) {
        return this.patente.compareToIgnoreCase(otro.patente);
    }

    // ================= ABSTRACTO =================

    public abstract double calcularPrecio();

    @Override
    public String toString() {
        return "Tipo: " + getClass().getSimpleName() +
                " | Patente: " + patente +
                " | Marca: " + marca +
                " | Año: " + anio +
                " | Precio: " + calcularPrecio();
    }
}