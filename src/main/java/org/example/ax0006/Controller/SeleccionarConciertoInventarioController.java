package org.example.ax0006.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ConciertoService;

public class SeleccionarConciertoInventarioController {

    private final ConciertoService conciertoService;
    private final SceneManager sceneManager;
    private final SesionManager sesion;

    @FXML private TableView<Concierto> tv_conciertos;
    @FXML private TableColumn<Concierto, Integer> col_id;
    @FXML private TableColumn<Concierto, String> col_nombre;
    @FXML private TableColumn<Concierto, String> col_fecha;
    @FXML private TableColumn<Concierto, String> col_estado;

    public SeleccionarConciertoInventarioController(ConciertoService conciertoService, SceneManager sceneManager, SesionManager sesion) {
        this.conciertoService = conciertoService;
        this.sceneManager = sceneManager;
        this.sesion = sesion;
    }

    @FXML
    public void initialize() {
        col_id.setCellValueFactory(new PropertyValueFactory<>("idConcierto"));
        col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombreConcierto"));

        col_fecha.setCellValueFactory(cellData -> {
            if (cellData.getValue().getHorario() != null) {
                return new SimpleStringProperty(cellData.getValue().getHorario().getFechaInicio().toString());
            }
            return new SimpleStringProperty("Sin fecha");
        });

        col_estado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isProgramado() ? "Programado" : "Pendiente")
        );

        cargarDatos();
    }

    private void cargarDatos() {
        tv_conciertos.setItems(FXCollections.observableArrayList(conciertoService.obtenerConciertos()));
    }

    @FXML
    void on_bt_seleccionar() {
        Concierto seleccionado = tv_conciertos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            sesion.setConciertoTemporal(seleccionado);
            try {
                sceneManager.showCrearInventario();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atención");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un concierto de la lista.");
            alert.showAndWait();
        }
    }

    @FXML
    void on_bt_volver() {
        try {
            sceneManager.showMenuConcierto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}