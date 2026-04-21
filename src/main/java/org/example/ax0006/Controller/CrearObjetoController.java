package org.example.ax0006.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ax0006.Entity.Inventario;
import org.example.ax0006.Entity.ModeloObjeto;
import org.example.ax0006.Entity.Objeto;
import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ObjetoService;

public class CrearObjetoController {

    @FXML private TableView<Objeto> tablaObjetos;
    @FXML private TableColumn<Objeto, Integer> colID;
    @FXML private TableColumn<Objeto, String> colModelo;
    @FXML private TableColumn<Objeto, String> colEstado;
    @FXML private TableColumn<Objeto, Boolean> colDisponible;

    @FXML private ComboBox<TipoObjeto> comboTipo;
    @FXML private ComboBox<ModeloObjeto> comboModelo;
    @FXML private TextField txtEstado;
    @FXML private TextField txtObs;

    private SceneManager sceneManager;
    private SesionManager sesion;
    private ObjetoService objetoService;


    public CrearObjetoController (SceneManager sceneManager, SesionManager sesion, ObjetoService objetoService){
        this.sceneManager = sceneManager;
        this.sesion = sesion;
        this.objetoService = objetoService;
    }

    @FXML
    public void initialize() {

        // ID
        colID.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().getIdObjeto()
                ).asObject()
        );

        // Modelo
        colModelo.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getModelo().getNombre()
                )
        );

        // Estado
        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado())
        );

        // Disponible
        colDisponible.setCellValueFactory(data ->
                new SimpleBooleanProperty(data.getValue().isDisponible())
        );

        comboTipo.setOnAction(e -> {
            TipoObjeto tipo = comboTipo.getValue();

            if (tipo != null) {
                comboModelo.getItems().setAll(
                        objetoService.obtenerModelosPorTipo(tipo.getIdTipoObjeto())
                );
            }
        });

        cargarObjetos();
        cargarTipos();
    }

    // ===== CARGA DATOS =====

    private void cargarTipos() {
        comboTipo.getItems().setAll(objetoService.obtenerTipos());
    }

    private void cargarObjetos() {

        tablaObjetos.getItems().setAll(
                objetoService.obtenerObjetosInventario(sesion.getInventarioSeleccionado().getIdInventario())
        );

    }

    @FXML
    void On_crearObjeto() {

        ModeloObjeto modelo = comboModelo.getValue();

        if (modelo == null) {
            mostrarError("Debe seleccionar un modelo");
            return;
        }

        if (txtEstado.getText().isBlank()) {
            mostrarError("Debe ingresar estado");
            return;
        }

        objetoService.crearObjeto(
                modelo.getIdModelo(),
                sesion.getInventarioSeleccionado().getIdInventario(),
                txtEstado.getText(),
                txtObs.getText()
        );

        limpiarFormulario();
        cargarObjetos(); // refresca tabla automáticamente
    }

    @FXML
    void On_eliminarObjeto() {

        Objeto obj = tablaObjetos.getSelectionModel().getSelectedItem();

        if (obj == null) {
            mostrarError("Seleccione un objeto");
            return;
        }

        objetoService.eliminarObjeto(obj.getIdObjeto());
        cargarObjetos();
    }

    private void limpiarFormulario() {
        comboTipo.getSelectionModel().clearSelection();
        comboModelo.getItems().clear();
        txtEstado.clear();
        txtObs.clear();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.show();
    }
}