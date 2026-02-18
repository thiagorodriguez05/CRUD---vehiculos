package gestionauto.gestionautos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {

    // =======================
    // FORMULARIO
    // =======================

    @FXML private TextField txtPatente;
    @FXML private TextField txtMarca;
    @FXML private TextField txtAnio;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExtra;

    @FXML private Label lblExtra;

    @FXML private ComboBox<TipoVehiculo> cmbTipo;
    @FXML private ComboBox<String> cbOrdenar;
    @FXML private ComboBox<String> cbFiltrar;

    @FXML private TableView<Vehiculo> tablaVehiculos;

    @FXML private TableColumn<Vehiculo, String> colPatente;
    @FXML private TableColumn<Vehiculo, String> colMarca;
    @FXML private TableColumn<Vehiculo, Integer> colAnio;
    @FXML private TableColumn<Vehiculo, Double> colPrecio;

    private ObservableList<Vehiculo> vehiculos;
    private FilteredList<Vehiculo> listaFiltrada;
    private SortedList<Vehiculo> listaOrdenada;

    private boolean cargandoFormulario = false;

    // =======================
    // INIT
    // =======================

@Override
public void initialize(URL url, ResourceBundle rb) {

    // =======================
    // LISTA BASE
    // =======================

    vehiculos = FXCollections.observableArrayList();

    try {
        vehiculos.addAll(ArchivoVehiculo.cargar());
    } catch (Exception e) {
        mostrarError("No se pudieron cargar los vehículos");
    }

    // =======================
    // FILTRADO Y ORDEN
    // =======================

    listaFiltrada = new FilteredList<>(vehiculos, p -> true);
    listaOrdenada = new SortedList<>(listaFiltrada);

    listaOrdenada.comparatorProperty().bind(tablaVehiculos.comparatorProperty());
    tablaVehiculos.setItems(listaOrdenada);

    // =======================
    // CARGAR TIPOS VEHICULO
    // =======================

    cmbTipo.setItems(FXCollections.observableArrayList(TipoVehiculo.values()));
    cmbTipo.getSelectionModel().selectFirst();

    cmbTipo.valueProperty().addListener((obs, oldVal, newVal) ->
            actualizarFormularioPorTipo(newVal)
    );

    // =======================
    // OPCIONES ORDENAR
    // =======================

    cbOrdenar.setItems(FXCollections.observableArrayList(
            "Patente", "Marca", "Año", "Precio"
    ));

    cbOrdenar.getSelectionModel().selectFirst();
    cbOrdenar.setOnAction(e -> aplicarOrden());

    // =======================
    // OPCIONES FILTRAR
    // =======================

    cbFiltrar.setItems(FXCollections.observableArrayList(
            "Todos", "Mayor a 50000", "Menor a 50000"
    ));

    cbFiltrar.getSelectionModel().selectFirst();
    cbFiltrar.setOnAction(e -> aplicarFiltro());

    // Aplicar por defecto
    aplicarOrden();
    aplicarFiltro();

    // =======================
    // COLUMNAS
    // =======================

    colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
    colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
    colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

    colPrecio.setCellValueFactory(data ->
            Bindings.createObjectBinding(
                    () -> data.getValue().calcularPrecio()
            )
    );

    tablaVehiculos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // =======================
    // LISTENER SELECCION TABLA
    // =======================

    tablaVehiculos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldV, newV) -> cargarFormulario(newV)
    );
}


    // =======================
    // AGREGAR
    // =======================

    @FXML
    private void agregarVehiculo() {

        try {
            Vehiculo v = crearVehiculoDesdeFormulario();

            for (Vehiculo existente : vehiculos) {
                if (existente.getPatente().equalsIgnoreCase(v.getPatente())) {
                    throw new VehiculoDuplicadoException("Ya existe un vehículo con esa patente");
                }
            }

            vehiculos.add(v);
            guardarDatos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // =======================
    // MODIFICAR
    // =======================

    @FXML
    private void modificarVehiculo() {

        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Seleccione un vehículo");
            return;
        }

        try {
            Vehiculo nuevo = crearVehiculoDesdeFormulario();

            for (Vehiculo v : vehiculos) {
                if (!v.equals(seleccionado) &&
                        v.getPatente().equalsIgnoreCase(nuevo.getPatente())) {
                    throw new VehiculoDuplicadoException("Ya existe un vehículo con esa patente");
                }
            }

            int index = vehiculos.indexOf(seleccionado);

            if (index == -1)
                throw new VehiculoNoEncontradoException("Vehículo no encontrado");

            vehiculos.set(index, nuevo);

            guardarDatos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // =======================
    // ELIMINAR
    // =======================

    @FXML
    private void eliminarVehiculo() {

        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Seleccione un vehículo");
            return;
        }

        vehiculos.remove(seleccionado);
        guardarDatos();
        limpiarFormulario();
    }

    // =======================
    // ORDENAR
    // =======================

private void aplicarOrden() {

    String criterio = cbOrdenar.getValue();

    tablaVehiculos.getSortOrder().clear();

    if (criterio == null) return;

    switch (criterio) {
        case "Patente":
            colPatente.setSortType(TableColumn.SortType.ASCENDING);
            tablaVehiculos.getSortOrder().add(colPatente);
            break;

        case "Marca":
            colMarca.setSortType(TableColumn.SortType.ASCENDING);
            tablaVehiculos.getSortOrder().add(colMarca);
            break;

        case "Año":
            colAnio.setSortType(TableColumn.SortType.ASCENDING);
            tablaVehiculos.getSortOrder().add(colAnio);
            break;

        case "Precio":
            colPrecio.setSortType(TableColumn.SortType.ASCENDING);
            tablaVehiculos.getSortOrder().add(colPrecio);
            break;
    }
}


    // =======================
    // FILTRAR
    // =======================

    private void aplicarFiltro() {

    String opcion = cbFiltrar.getValue();
    if (opcion == null) return;

    listaFiltrada.setPredicate(vehiculo -> {

        switch (opcion) {

            case "Mayor a 50000":
                return vehiculo.calcularPrecio() > 50000;

            case "Menor a 50000":
                return vehiculo.calcularPrecio() < 50000;

            default:
                return true;
        }
    });
}


    // =======================
    // EXPORTAR
    // =======================

    @FXML
    private void exportarCSV() {
        try {
            ArchivoVehiculo.exportarCSV(new ArrayList<>(vehiculos));
        } catch (IOException e) {
            mostrarError("Error al exportar CSV");
        }
    }

    @FXML
    private void exportarJSON() {
        try {
            ArchivoVehiculo.exportarJSON(new ArrayList<>(vehiculos));
        } catch (IOException e) {
            mostrarError("Error al exportar JSON");
        }
    }

    @FXML
    private void exportarTXT() {
        try {
            ArchivoVehiculo.exportarTXT(new ArrayList<>(vehiculos));
        } catch (IOException e) {
            mostrarError("Error al exportar TXT");
        }
    }

    // =======================
    // GUARDADO
    // =======================

    private void guardarDatos() {
        try {
            ArchivoVehiculo.guardar(new ArrayList<>(vehiculos));
        } catch (IOException e) {
            mostrarError("Error al guardar archivo");
        }
    }

    // =======================
    // FORMULARIO
    // =======================

private Vehiculo crearVehiculoDesdeFormulario() {

    String patente = txtPatente.getText().trim();
    String marca = txtMarca.getText().trim();
    String anioTexto = txtAnio.getText().trim();
    String precioTexto = txtPrecio.getText().trim();
    String extraTexto = txtExtra.getText().trim();

    // =====================
    // VALIDACIONES BASICAS
    // =====================

    if (patente.isEmpty())
        throw new IllegalArgumentException("La patente es obligatoria");

    if (!patente.matches("^[A-Za-z0-9]{6,7}$"))
        throw new IllegalArgumentException("Formato de patente inválido");

    if (marca.isEmpty())
        throw new IllegalArgumentException("La marca es obligatoria");

    if (!marca.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
        throw new IllegalArgumentException("La marca solo puede contener letras");

    if (anioTexto.isEmpty())
        throw new IllegalArgumentException("El año es obligatorio");

    if (precioTexto.isEmpty())
        throw new IllegalArgumentException("El precio es obligatorio");

    if (extraTexto.isEmpty())
        throw new IllegalArgumentException("El campo adicional es obligatorio");

    try {

        int anio = Integer.parseInt(anioTexto);
        double precio = Double.parseDouble(precioTexto);

        // =====================
        // VALIDACIONES LOGICAS
        // =====================

        if (anio < 1900 || anio > 2026)
            throw new IllegalArgumentException("El año no es válido");

        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a 0");

        TipoVehiculo tipo = cmbTipo.getValue();

        switch (tipo) {

            case AUTO:
                int puertas = Integer.parseInt(extraTexto);
                if (puertas <= 0)
                    throw new IllegalArgumentException("Cantidad de puertas inválida");
                return new Auto(patente, marca, anio, puertas, precio);

            case MOTO:
                int cilindrada = Integer.parseInt(extraTexto);
                if (cilindrada <= 0)
                    throw new IllegalArgumentException("Cilindrada inválida");
                return new Moto(patente, marca, anio, cilindrada, precio);

            case CAMION:
                double carga = Double.parseDouble(extraTexto);
                if (carga <= 0)
                    throw new IllegalArgumentException("Carga máxima inválida");
                return new Camion(patente, marca, anio, carga, precio);

            default:
                throw new IllegalArgumentException("Tipo inválido");
        }

    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Campos numéricos inválidos");
    }
}


   private void cargarFormulario(Vehiculo v) {

    if (v == null) return;

    cargandoFormulario = true;

    txtPatente.setText(v.getPatente());
    txtMarca.setText(v.getMarca());
    txtAnio.setText(String.valueOf(v.getAnio()));
    txtPrecio.setText(String.valueOf(v.getPrecioBase()));

    if (v instanceof Auto) {

        Auto a = (Auto) v;
        cmbTipo.setValue(TipoVehiculo.AUTO);
        txtExtra.setText(String.valueOf(a.getPuertas()));

    } else if (v instanceof Moto) {

        Moto m = (Moto) v;
        cmbTipo.setValue(TipoVehiculo.MOTO);
        txtExtra.setText(String.valueOf(m.getCilindrada()));

    } else if (v instanceof Camion) {

        Camion c = (Camion) v;
        cmbTipo.setValue(TipoVehiculo.CAMION);
        txtExtra.setText(String.valueOf(c.getCargaMaxima()));
    }

    cargandoFormulario = false;
}


private void actualizarFormularioPorTipo(TipoVehiculo tipo) {

    if (tipo == null || cargandoFormulario) return;

    txtExtra.clear();

    switch (tipo) {

        case AUTO:
            lblExtra.setText("Puertas");
            break;

        case MOTO:
            lblExtra.setText("Cilindrada");
            break;

        case CAMION:
            lblExtra.setText("Carga Máxima");
            break;
    }
}


    private void limpiarFormulario() {
        txtPatente.clear();
        txtMarca.clear();
        txtAnio.clear();
        txtPrecio.clear();
        txtExtra.clear();
        tablaVehiculos.getSelectionModel().clearSelection();
        cmbTipo.getSelectionModel().selectFirst();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ocurrió un problema");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
