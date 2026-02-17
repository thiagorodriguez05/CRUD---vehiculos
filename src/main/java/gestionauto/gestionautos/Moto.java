package gestionauto.gestionautos;

import java.io.Serializable;

public class Moto extends Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int cilindrada;

    // CONSTRUCTOR PRINCIPAL
    public Moto(String patente, String marca, int anio,
                int cilindrada, double precioBase) {

        super(patente, marca, anio, precioBase);
        this.cilindrada = cilindrada;
    }

    // CONSTRUCTOR SIN PRECIO (precio por defecto)
    public Moto(String patente, String marca, int anio, int cilindrada) {
        this(patente, marca, anio, cilindrada, 800_000);
    }

    // CONSTRUCTOR SIMPLE
    public Moto(String patente) {
        this(patente, "Genérica", 2023, 150);
    }

    public int getCilindrada() {
        return cilindrada;
    }

    @Override
    public double calcularPrecio() {
        return precioBase + (cilindrada * 1_000);
    }
}
