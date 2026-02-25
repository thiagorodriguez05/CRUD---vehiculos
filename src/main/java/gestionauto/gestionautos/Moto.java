package gestionauto.gestionautos;

import java.io.Serializable;

public class Moto extends Vehiculo implements Serializable{

    private static final long serialVersionUID = 1L;
    private static final double VALOR_POR_CC = 1000;

    private int cilindrada;
    private String tipoMoto;

    public Moto(String patente, String marca, int anio,
                int cilindrada, String tipoMoto, double precioBase) {

        super(patente, marca, anio, precioBase);

        if (cilindrada <= 0)
            throw new IllegalArgumentException("La cilindrada debe ser mayor a 0");

        if (tipoMoto == null || tipoMoto.isBlank())
            throw new IllegalArgumentException("El tipo de moto es obligatorio");

        this.cilindrada = cilindrada;
        this.tipoMoto = tipoMoto.trim();

        validarTipoMoto(this.tipoMoto);
    }

    private void validarTipoMoto(String tipo) {

        if (!tipo.equalsIgnoreCase("Urbana") &&
            !tipo.equalsIgnoreCase("Deportiva") &&
            !tipo.equalsIgnoreCase("Cross") &&
            !tipo.equalsIgnoreCase("Touring")) {

            throw new IllegalArgumentException("Tipo de moto inválido");
        }
    }

    public Moto(String patente, String marca, int anio,
                int cilindrada, double precioBase) {

        this(patente, marca, anio, cilindrada, "Urbana", precioBase);
    }

    public Moto(String patente, String marca, int anio,
                int cilindrada, String tipoMoto) {

        this(patente, marca, anio, cilindrada, tipoMoto, 800000);
    }

    public Moto(String patente) {
        this(patente, "Genérica", 2023, 150, "Urbana");
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public String getTipoMoto() {
        return tipoMoto;
    }

    @Override
    public double calcularPrecio() {
        return precioBase + (cilindrada * VALOR_POR_CC);
    }

    @Override
    public String toString() {
        return super.toString() +
               " | Cilindrada: " + cilindrada +
               " | Tipo Moto: " + tipoMoto;
    }
}