package gestionauto.gestionautos;

import java.io.Serializable;

public class Camion extends Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    private double cargaMaxima;

    // CONSTRUCTOR PRINCIPAL
    public Camion(String patente, String marca, int anio,
                  double cargaMaxima, double precioBase) {

        super(patente, marca, anio, precioBase);
        this.cargaMaxima = cargaMaxima;
    }

    // CONSTRUCTOR SIN PRECIO (precio por defecto)
    public Camion(String patente, String marca, int anio, double cargaMaxima) {
        this(patente, marca, anio, cargaMaxima, 3_000_000);
    }

    // CONSTRUCTOR SIMPLE
    public Camion(String patente) {
        this(patente, "Genérico", 2021, 10);
    }

    public double getCargaMaxima() {
        return cargaMaxima;
    }

    @Override
    public double calcularPrecio() {
        return precioBase + (cargaMaxima * 50_000);
    }
}
