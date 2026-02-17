package gestionauto.gestionautos;

import java.util.Comparator;

public class VehiculoAnioComparator implements Comparator<Vehiculo> {

    @Override
    public int compare(Vehiculo v1, Vehiculo v2) {
        return Integer.compare(v1.getAnio(), v2.getAnio());
    }
}
