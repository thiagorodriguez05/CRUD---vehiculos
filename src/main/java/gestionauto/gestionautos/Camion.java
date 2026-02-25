package gestionauto.gestionautos;

import java.io.Serializable;

public class Camion extends Vehiculo implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final double VALOR_POR_TONELADA = 50000;

    private double cargaMaxima;
    private String tipoCabina;

    public Camion(String patente, String marca, int anio,
                  double cargaMaxima, String tipoCabina, double precioBase) {

        super(patente, marca, anio, precioBase);

        if (cargaMaxima <= 0)
            throw new IllegalArgumentException("La carga máxima debe ser mayor a 0");

        if (tipoCabina == null || tipoCabina.isBlank())
            throw new IllegalArgumentException("El tipo de cabina es obligatorio");

        this.cargaMaxima = cargaMaxima;
        this.tipoCabina = tipoCabina.trim();

        validarCabina(this.tipoCabina);
    }

    private void validarCabina(String cabina) {

        if (!cabina.equalsIgnoreCase("Simple") &&
            !cabina.equalsIgnoreCase("Doble") &&
            !cabina.equalsIgnoreCase("Dormitorio")) {

            throw new IllegalArgumentException("Tipo de cabina inválido");
        }
    }

    public Camion(String patente, String marca, int anio,
                  double cargaMaxima, double precioBase) {

        this(patente, marca, anio, cargaMaxima, "Simple", precioBase);
    }

    public Camion(String patente, String marca, int anio,
                  double cargaMaxima, String tipoCabina) {

        this(patente, marca, anio, cargaMaxima, tipoCabina, 3000000);
    }

    public Camion(String patente) {
        this(patente, "Genérico", 2021, 10, "Simple");
    }

    public double getCargaMaxima() {
        return cargaMaxima;
    }

    public String getTipoCabina() {
        return tipoCabina;
    }

    @Override
    public double calcularPrecio() {
        return precioBase + (cargaMaxima * VALOR_POR_TONELADA);
    }

    @Override
    public String toString() {
        return super.toString() +
               " | Carga Máxima: " + cargaMaxima +
               " | Tipo Cabina: " + tipoCabina;
    }
}