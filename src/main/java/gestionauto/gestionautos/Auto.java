package gestionauto.gestionautos;

import java.io.Serializable;

public class Auto extends Vehiculo implements Serializable{

    private static final long serialVersionUID = 1L;

    private int puertas;
    private String tipoCombustible;

    public Auto(String patente, String marca, int anio,
                int puertas, String tipoCombustible, double precioBase) {

        super(patente, marca, anio, precioBase);

        //validacion
        if (puertas <2 || puertas >4)
            throw new IllegalArgumentException("La cantidad de puertas debe ser entre 2 y 4");

        if (tipoCombustible == null || tipoCombustible.isBlank())
            throw new IllegalArgumentException("El tipo de combustible es obligatorio");

        this.puertas = puertas;
        this.tipoCombustible = tipoCombustible.trim();

        validarCombustible(this.tipoCombustible);
    }

    // Método privado para validar combustible
    private void validarCombustible(String combustible) {

        if (!combustible.equalsIgnoreCase("Nafta") &&
            !combustible.equalsIgnoreCase("Diesel") &&
            !combustible.equalsIgnoreCase("Electrico")) {

            throw new IllegalArgumentException("Tipo de combustible inválido");
        }
    }

    // Constructor formulario
    public Auto(String patente, String marca, int anio,
                int puertas, double precioBase) {

        this(patente, marca, anio, puertas, "Nafta", precioBase);
    }

    // Constructor sin precio
    public Auto(String patente, String marca, int anio,
                int puertas, String tipoCombustible) {

        this(patente, marca, anio, puertas, tipoCombustible, 1000000);
    }

    public Auto(String patente) {
        this(patente, "Genérico", 2022, 4, "Nafta");
    }

    public int getPuertas() {
        return puertas;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    @Override
    public double calcularPrecio() {

        double precio = precioBase;

        if (puertas > 2 )
            precio += 200000;

        if (tipoCombustible.equalsIgnoreCase("Diesel"))
            precio += 150000;

        if (tipoCombustible.equalsIgnoreCase("Electrico"))
            precio += 300000;

        return precio;
    }

    @Override
    public String toString() {
        return super.toString() +
               " | Puertas: " + puertas +
               " | Combustible: " + tipoCombustible;
    }
}