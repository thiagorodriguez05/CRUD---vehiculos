package gestionauto.gestionautos;

import java.util.Comparator;

public class VehiculoMarcaComparator implements Comparator<Vehiculo> {

    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return v1.getMarca().compareToIgnoreCase(v2.getMarca());
    }
}
