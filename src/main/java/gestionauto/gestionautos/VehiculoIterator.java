package gestionauto.gestionautos;

import java.util.Iterator;
import java.util.List;

public class VehiculoIterator implements Iterator<Vehiculo> {

    private int indice = 0;
    private List<Vehiculo> lista;

    public VehiculoIterator(List<Vehiculo> lista) {
        this.lista = lista;
    }

    @Override
    public boolean hasNext() {
        return indice < lista.size();
    }

    @Override
    public Vehiculo next() {
        return lista.get(indice++);
    }
}
