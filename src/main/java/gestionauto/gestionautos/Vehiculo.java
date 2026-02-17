package gestionauto.gestionautos;

import java.io.Serializable;

public abstract class Vehiculo
        implements Comparable<Vehiculo>, Serializable {

    private static final long serialVersionUID = 1L;

    protected String patente;
    protected String marca;
    protected int anio;
    protected double precioBase;

    // CONSTRUCTOR PRINCIPAL
    public Vehiculo(String patente, String marca, int anio,
                    double precioBase) {
        this.patente = patente;
        this.marca = marca;
        this.anio = anio;
        this.precioBase = precioBase;
    }

    // CONSTRUCTOR SIN PRECIO
    public Vehiculo(String patente, String marca, int anio) {
        this(patente, marca, anio, 0);
    }

    // CONSTRUCTOR SIMPLE
    public Vehiculo(String patente) {
        this(patente, "Sin marca", 0, 0);
    }

    // ========= GETTERS =========
    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public int getAnio() {
        return anio;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public double getPrecioFinal() {
        return calcularPrecio();
    }

    // ========= EQUALS / HASHCODE =========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehiculo)) return false;
        Vehiculo vehiculo = (Vehiculo) o;
        return patente.equalsIgnoreCase(vehiculo.patente);
    }

    @Override
    public int hashCode() {
        return patente.toLowerCase().hashCode();
    }

    // ========= ORDEN NATURAL =========
    @Override
    public int compareTo(Vehiculo otro) {
        return this.patente.compareToIgnoreCase(otro.patente);
    }

    // ========= ABSTRACTO =========
    public abstract double calcularPrecio();
}
