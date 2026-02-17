package gestionauto.gestionautos;

import java.io.Serializable;

public class Auto extends Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int puertas;

    // CONSTRUCTOR PRINCIPAL
    public Auto(String patente, String marca, int anio,
                int puertas, double precioBase) {

        super(patente, marca, anio, precioBase);
        this.puertas = puertas;
    }

    // CONSTRUCTOR SIN PRECIO (precio por defecto)
    public Auto(String patente, String marca, int anio, int puertas) {
        this(patente, marca, anio, puertas, 1_000_000);
    }

    // CONSTRUCTOR SIMPLE
    public Auto(String patente) {
        this(patente, "Genérico", 2022, 4);
    }

    public int getPuertas() {
        return puertas;
    }

    @Override
    public double calcularPrecio() {
        return precioBase;
    }
}
