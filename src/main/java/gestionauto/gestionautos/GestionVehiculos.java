package gestionauto.gestionautos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GestionVehiculos implements ICrud<Vehiculo>, Iterable<Vehiculo> {

    private List<Vehiculo> listaVehiculos;

    public GestionVehiculos() {
        this.listaVehiculos = new ArrayList<>();
    }

    // =========================
    // CRUD
    // =========================

    @Override
    public void crear(Vehiculo vehiculo) {

        if (vehiculo == null)
            throw new IllegalArgumentException("El vehículo no puede ser null");

        if (listaVehiculos.contains(vehiculo))
            throw new VehiculoDuplicadoException(
                    "Ya existe un vehículo con la patente: "
                            + vehiculo.getPatente());

        listaVehiculos.add(vehiculo);
    }

    @Override
    public List<Vehiculo> leer() {
        return new ArrayList<>(listaVehiculos);
    }

    @Override
    public void actualizar(Vehiculo vehiculo) {

        if (vehiculo == null)
            throw new IllegalArgumentException("El vehículo no puede ser null");

        for (int i = 0; i < listaVehiculos.size(); i++) {

            if (listaVehiculos.get(i).equals(vehiculo)) {
                listaVehiculos.set(i, vehiculo);
                return;
            }
        }

        throw new VehiculoNoEncontradoException(
                "No se encontró el vehículo para actualizar: "
                        + vehiculo.getPatente());
    }

    @Override
    public void eliminar(Vehiculo vehiculo) {

        if (vehiculo == null)
            throw new IllegalArgumentException("El vehículo no puede ser null");

        boolean eliminado = listaVehiculos.remove(vehiculo);

        if (!eliminado) {
            throw new VehiculoNoEncontradoException(
                    "No se encontró el vehículo para eliminar: "
                            + vehiculo.getPatente());
        }
    }

    // =========================
    // BÚSQUEDA
    // =========================

    public Vehiculo buscarPorPatente(String patente) {

        if (patente == null || patente.isBlank())
            throw new IllegalArgumentException("La patente no puede ser null o vacía");

        for (Vehiculo v : listaVehiculos) {

            if (v.getPatente().equalsIgnoreCase(patente)) {
                return v;
            }
        }

        throw new VehiculoNoEncontradoException(
                "No se encontró el vehículo con patente: " + patente);
    }

    // =========================
    // FILTRO (? extends)
    // =========================

    public List<? extends Vehiculo> filtrarPorTipo(
            Class<? extends Vehiculo> tipo) {

        List<Vehiculo> filtrados = new ArrayList<>();

        for (Vehiculo v : listaVehiculos) {

            if (tipo.isInstance(v)) {
                filtrados.add(v);
            }
        }

        return filtrados;
    }

    // =========================
    // WILDCARD (? super)
    // =========================

    public void agregarATipoGeneral(
            List<? super Vehiculo> listaDestino,
            Vehiculo vehiculo) {

        listaDestino.add(vehiculo);
    }

    // =========================
    // ITERABLE
    // =========================

    @Override
    public Iterator<Vehiculo> iterator() {
        return listaVehiculos.iterator();
    }

    // =========================
    // LAMBDAS
    // =========================

    public void aplicarCambio(Consumer<Vehiculo> accion) {
        for (Vehiculo v : listaVehiculos) {
            accion.accept(v);
        }
    }

    public void transformarVehiculos(
            Function<Vehiculo, Vehiculo> funcion) {

        for (int i = 0; i < listaVehiculos.size(); i++) {
            listaVehiculos.set(i,
                    funcion.apply(listaVehiculos.get(i)));
        }
    }
}