package gestionauto.gestionautos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class GestionVehiculos implements ICrud<Vehiculo>, Iterable<Vehiculo> {

    private List<Vehiculo> listaVehiculos;

    public GestionVehiculos() {
        listaVehiculos = new ArrayList<>();
    }

    // =========================
    // CRUD 
    // =========================

    @Override
    public void crear(Vehiculo vehiculo) {
        listaVehiculos.add(vehiculo);
    }

    @Override
    public List<Vehiculo> leer() {
        return listaVehiculos;
    }

    @Override
    public void actualizar(Vehiculo vehiculo) {
        for (int i = 0; i < listaVehiculos.size(); i++) {
            if (listaVehiculos.get(i).getPatente()
                    .equalsIgnoreCase(vehiculo.getPatente())) {

                listaVehiculos.set(i, vehiculo);
                return;
            }
        }
    }

    @Override
    public void eliminar(Vehiculo vehiculo) {
        listaVehiculos.removeIf(v ->
                v.getPatente().equalsIgnoreCase(vehiculo.getPatente()));
    }

    // =========================
    // BÚSQUEDA CON EXCEPCIÓN PROPIA
    // =========================

    public Vehiculo buscarPorPatente(String patente)
            throws VehiculoNoEncontradoException {

        for (Vehiculo v : listaVehiculos) {
            if (v.getPatente().equalsIgnoreCase(patente)) {
                return v;
            }
        }

        throw new VehiculoNoEncontradoException(
                "No se encontró el vehículo con patente: " + patente);
    }

    // =========================
    // FILTRO CON WILDCARD (? extends)
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
    // ITERATOR PERSONALIZADO
    // =========================

    @Override
    public Iterator<Vehiculo> iterator() {
        return new VehiculoIterator(listaVehiculos);
    }

    // =========================
    // LAMBDAS / INTERFACES FUNCIONALES
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
