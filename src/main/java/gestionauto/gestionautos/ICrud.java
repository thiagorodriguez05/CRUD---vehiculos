package gestionauto.gestionautos;

import java.util.List;

public interface ICrud<T> {

    void crear(T elemento);

    List<T> leer();

    void actualizar(T elemento);

    void eliminar(T elemento);
}
