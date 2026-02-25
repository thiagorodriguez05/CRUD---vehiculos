package gestionauto.gestionautos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    // ================= FORMULARIO =================

    @FXML private TextField txtPatente;
    @FXML private TextField txtMarca;
    @FXML private TextField txtAnio;
    @FXML private TextField txtPrecio;

    @FXML private TextField txtExtra1;
    @FXML private TextField txtExtra2;

    @FXML private Label lblExtra1;
    @FXML private Label lblExtra2;

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

    // ================= INIT =================

@Override
public void initialize(URL url, ResourceBundle rb) {

    // ================= CARGAR DATOS =================
    try {
        vehiculos = FXCollections.observableArrayList(ArchivoVehiculo.cargar());
        System.out.println("Vehículos cargados: " + vehiculos.size());
    } catch (Exception e) {
        e.printStackTrace();
        vehiculos = FXCollections.observableArrayList();
        mostrarError("No se pudieron cargar los vehículos");
    }

    // ================= FILTRADO Y ORDEN =================
    listaFiltrada = new FilteredList<>(vehiculos, p -> true);
    listaOrdenada = new SortedList<>(listaFiltrada);
    tablaVehiculos.setItems(listaOrdenada);

    // ================= COLUMNAS =================
    colPatente.setCellValueFactory(new PropertyValueFactory<>("patente"));
    colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
    colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

    colPrecio.setCellValueFactory(data ->
            Bindings.createObjectBinding(() -> data.getValue().calcularPrecio())
    );

    tablaVehiculos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    // ================= TIPOS =================
    cmbTipo.setItems(FXCollections.observableArrayList(TipoVehiculo.values()));
    cmbTipo.getSelectionModel().selectFirst();
    cmbTipo.valueProperty().addListener((obs, o, n) -> actualizarFormularioPorTipo(n));
    actualizarFormularioPorTipo(cmbTipo.getValue());

    // ================= ORDENAR =================
    cbOrdenar.setItems(FXCollections.observableArrayList("Patente", "Marca", "Año", "Precio"));
    cbOrdenar.getSelectionModel().selectFirst();
    cbOrdenar.setOnAction(e -> aplicarOrden());

    // ================= FILTRAR =================
    cbFiltrar.setItems(FXCollections.observableArrayList("Todos", "Mayor a 50000", "Menor a 50000"));
    cbFiltrar.getSelectionModel().selectFirst();
    cbFiltrar.setOnAction(e -> aplicarFiltro());

    aplicarOrden();
    aplicarFiltro();

    // ================= SELECCIÓN TABLA =================
    tablaVehiculos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldV, newV) -> {
                if (cargandoFormulario) return; // evita recursión
                if (newV != null) {
                    cargarFormulario(newV);
                }
            }
    );

    // ================= CLICK EN FILAS (ÁREA VACÍA) =================
    tablaVehiculos.setRowFactory(tv -> {
        TableRow<Vehiculo> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (row.isEmpty()) {
                tablaVehiculos.getSelectionModel().clearSelection();
                limpiarFormulario();
            }
        });
        return row;
    });
}

    // ================= AGREGAR =================
    @FXML
    private void agregarVehiculo() {

        try {
            Vehiculo v = crearVehiculoDesdeFormulario();

            for (Vehiculo existente : vehiculos) {
                if (existente.getPatente().equalsIgnoreCase(v.getPatente())) {
                    throw new RuntimeException("Ya existe un vehículo con esa patente");
                }
            }

            vehiculos.add(v);
            guardarDatos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // ================= MODIFICAR =================

    @FXML
    private void modificarVehiculo() {

        Vehiculo seleccionado = tablaVehiculos.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarError("Seleccione un vehículo");
            return;
        }

        try {
            Vehiculo nuevo = crearVehiculoDesdeFormulario();
            int index = vehiculos.indexOf(seleccionado);
            vehiculos.set(index, nuevo);

            guardarDatos();
            limpiarFormulario();

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    // ================= ELIMINAR =================

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

    // ================= CREAR VEHICULO =================

    private Vehiculo crearVehiculoDesdeFormulario() {

        String patente = txtPatente.getText().trim();
        String marca = txtMarca.getText().trim();
        int anio = Integer.parseInt(txtAnio.getText().trim());
        double precio = Double.parseDouble(txtPrecio.getText().trim());

        String extra1 = txtExtra1.getText().trim();
        String extra2 = txtExtra2.getText().trim();

        TipoVehiculo tipo = cmbTipo.getValue();

        switch (tipo) {

            case AUTO:
                return new Auto(
                        patente,
                        marca,
                        anio,
                        Integer.parseInt(extra1),
                        extra2,
                        precio
                );

            case MOTO:
                return new Moto(
                        patente,
                        marca,
                        anio,
                        Integer.parseInt(extra1),
                        extra2,
                        precio
                );

            case CAMION:
                return new Camion(
                        patente,
                        marca,
                        anio,
                        Double.parseDouble(extra1),
                        extra2,
                        precio
                );

            default:
                throw new IllegalArgumentException("Tipo inválido");
        }
    }

    // ================= FORMULARIO =================

    private void actualizarFormularioPorTipo(TipoVehiculo tipo) {

        if (tipo == null || cargandoFormulario) return;

        txtExtra1.clear();
        txtExtra2.clear();

        switch (tipo) {

            case AUTO:
                lblExtra1.setText("Puertas");
                lblExtra2.setText("Tipo Combustible");
                break;

            case MOTO:
                lblExtra1.setText("Cilindrada");
                lblExtra2.setText("Tipo Moto");
                break;

            case CAMION:
                lblExtra1.setText("Carga Máxima");
                lblExtra2.setText("Tipo Cabina");
                break;
        }
    }

    private void cargarFormulario(Vehiculo v) {

        cargandoFormulario = true;

        txtPatente.setText(v.getPatente());
        txtMarca.setText(v.getMarca());
        txtAnio.setText(String.valueOf(v.getAnio()));
        txtPrecio.setText(String.valueOf(v.getPrecioBase()));

        if (v instanceof Auto) {
            Auto a = (Auto) v;
            cmbTipo.setValue(TipoVehiculo.AUTO);
            txtExtra1.setText(String.valueOf(a.getPuertas()));
            txtExtra2.setText(a.getTipoCombustible());
        }
        else if (v instanceof Moto) {
            Moto m = (Moto) v;
            cmbTipo.setValue(TipoVehiculo.MOTO);
            txtExtra1.setText(String.valueOf(m.getCilindrada()));
            txtExtra2.setText(m.getTipoMoto());
        }
        else if (v instanceof Camion) {
            Camion c = (Camion) v;
            cmbTipo.setValue(TipoVehiculo.CAMION);
            txtExtra1.setText(String.valueOf(c.getCargaMaxima()));
            txtExtra2.setText(c.getTipoCabina());
        }

        txtPatente.setDisable(true);
        cmbTipo.setDisable(true);

        cargandoFormulario = false;
    }

    private void limpiarFormulario() {

        txtPatente.clear();
        txtMarca.clear();
        txtAnio.clear();
        txtPrecio.clear();
        txtExtra1.clear();
        txtExtra2.clear();

        tablaVehiculos.getSelectionModel().clearSelection();
        cmbTipo.getSelectionModel().selectFirst();

        txtPatente.setDisable(false);
        cmbTipo.setDisable(false);
    }

    // ================= ORDEN =================

    private void aplicarOrden() {

        String criterio = cbOrdenar.getValue();

        switch (criterio) {

            case "Patente":
                listaOrdenada.setComparator(
                        (v1, v2) -> v1.getPatente().compareToIgnoreCase(v2.getPatente())
                );
                break;

            case "Marca":
                listaOrdenada.setComparator(
                        (v1, v2) -> v1.getMarca().compareToIgnoreCase(v2.getMarca())
                );
                break;

            case "Año":
                listaOrdenada.setComparator(
                        (v1, v2) -> Integer.compare(v1.getAnio(), v2.getAnio())
                );
                break;

            case "Precio":
                listaOrdenada.setComparator(
                        (v1, v2) -> Double.compare(
                                v1.calcularPrecio(),
                                v2.calcularPrecio()
                        )
                );
                break;
        }
    }
    
    // ================= FILTRO =================

    private void aplicarFiltro() {

        listaFiltrada.setPredicate(v -> {

            switch (cbFiltrar.getValue()) {

                case "Mayor a 50000":
                    return v.calcularPrecio() > 50000;

                case "Menor a 50000":
                    return v.calcularPrecio() < 50000;

                default:
                    return true;
            }
        });
    }

    // ================= EXPORTAR =================

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

    // ================= GUARDAR =================

    private void guardarDatos() {
        try {
            ArchivoVehiculo.guardar(new ArrayList<>(vehiculos));
        } catch (IOException e) {
            mostrarError("Error al guardar archivo");
        }
    }

    // ================= ALERTA =================

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Ocurrió un problema");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}