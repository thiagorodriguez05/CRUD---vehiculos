package gestionauto.gestionautos;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivoVehiculo {

    private static final String ARCHIVO_DAT = "vehiculos.dat";
    private static final String ARCHIVO_CSV = "vehiculos.csv";
    private static final String ARCHIVO_TXT = "vehiculos.txt";
    private static final String ARCHIVO_JSON = "vehiculos.json";

    // =========================
    // GUARDAR BINARIO (DAT)
    // =========================
    public static void guardar(List<? extends Vehiculo> lista) throws IOException {

        File file = new File(ARCHIVO_DAT);
        System.out.println("Guardando en: " + file.getAbsolutePath());

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file)
        );

        oos.writeObject(lista);
        oos.close();
    }

    // =========================
    // CARGAR BINARIO (DAT)
    // =========================
    @SuppressWarnings("unchecked")
    public static List<Vehiculo> cargar() throws IOException, ClassNotFoundException {

        File file = new File(ARCHIVO_DAT);
        System.out.println("Cargando desde: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("El archivo no existe todavía.");
            return new ArrayList<>();
        }

        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file)
        );

        List<Vehiculo> lista = (List<Vehiculo>) ois.readObject();
        ois.close();

        System.out.println("Vehículos leídos del archivo: " + lista.size());

        return lista;
    }

    // =========================
    // EXPORTAR CSV
    // =========================
    public static void exportarCSV(List<? extends Vehiculo> lista) throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_CSV));

        pw.println("Tipo,Patente,Marca,Anio,Precio");

        for (Vehiculo v : lista) {
            pw.println(
                    v.getClass().getSimpleName() + "," +
                    v.getPatente() + "," +
                    v.getMarca() + "," +
                    v.getAnio() + "," +
                    v.calcularPrecio()
            );
        }

        pw.close();
    }

    // =========================
    // EXPORTAR TXT
    // =========================
    public static void exportarTXT(List<? extends Vehiculo> lista) throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_TXT));

        for (Vehiculo v : lista) {
            pw.println(v.toString());
        }

        pw.close();
    }

    // =========================
    // EXPORTAR JSON
    // =========================
    public static void exportarJSON(List<? extends Vehiculo> lista) throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO_JSON));

        pw.println("[");

        for (int i = 0; i < lista.size(); i++) {

            Vehiculo v = lista.get(i);

            pw.println("  {");
            pw.println("    \"tipo\": \"" + v.getClass().getSimpleName() + "\",");
            pw.println("    \"patente\": \"" + v.getPatente() + "\",");
            pw.println("    \"marca\": \"" + v.getMarca() + "\",");
            pw.println("    \"anio\": " + v.getAnio() + ",");
            pw.println("    \"precio\": " + v.calcularPrecio());
            pw.print("  }");

            if (i < lista.size() - 1) {
                pw.println(",");
            } else {
                pw.println();
            }
        }

        pw.println("]");
        pw.close();
    }
}