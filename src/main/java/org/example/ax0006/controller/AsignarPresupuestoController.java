package org.example.ax0006.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.ax0006.entity.AnalisisFinanciero;
import org.example.ax0006.entity.Concierto;
import org.example.ax0006.manager.SceneManager;
import org.example.ax0006.service.AnalisisFinancieroService;
import org.example.ax0006.service.ConciertoService;

public class AsignarPresupuestoController {

    // =========================
    // SERVICES
    // =========================
    private AnalisisFinancieroService analisisService;
    private ConciertoService conciertoService;
    private SceneManager sceneManager;

    // =========================
    // CONSTRUCTOR
    // =========================
    public AsignarPresupuestoController(
            AnalisisFinancieroService analisisService,
            ConciertoService conciertoService,
            SceneManager sceneManager
    ) {

        this.analisisService = analisisService;
        this.conciertoService = conciertoService;
        this.sceneManager = sceneManager;
    }

    // =========================
    // TABLA ANALISIS
    // =========================
    @FXML
    private TableView<AnalisisFinanciero> tablaFinanzas;

    @FXML
    private TableColumn<AnalisisFinanciero, Integer> colId;

    @FXML
    private TableColumn<AnalisisFinanciero, Integer> colPresupuesto;

    @FXML
    private TableColumn<AnalisisFinanciero, Boolean> colAprobado;

    // =========================
    // COMBO CONCIERTOS
    // =========================
    @FXML
    private ComboBox<Concierto> comboConciertos;

    // =========================
    // INITIALIZE
    // =========================
    @FXML
    public void initialize() {

        // COLUMNAS
        colId.setCellValueFactory(
                data -> new SimpleObjectProperty<>(
                        data.getValue().getIdAnalisisF()
                )
        );

        colPresupuesto.setCellValueFactory(
                data -> new SimpleObjectProperty<>(
                        data.getValue().getPresupuesto()
                )
        );

        colAprobado.setCellValueFactory(
                data -> new SimpleObjectProperty<>(
                        data.getValue().isAprobado()
                )
        );

        // TABLA
        ObservableList<AnalisisFinanciero> listaFinanzas =
                FXCollections.observableArrayList(
                        analisisService.listarAnalisis()
                );

        tablaFinanzas.setItems(listaFinanzas);

        // COMBO CONCIERTOS
        ObservableList<Concierto> conciertos =
                FXCollections.observableArrayList(
                        conciertoService.obtenerConciertosSolos()
                );

        comboConciertos.setItems(conciertos);

        // MOSTRAR NOMBRE EN COMBOBOX
        comboConciertos.setCellFactory(param ->
                new ListCell<>() {
                    @Override
                    protected void updateItem(
                            Concierto item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(
                                    item.getNombreConcierto()
                            );
                        }
                    }
                });

        comboConciertos.setButtonCell(
                new ListCell<>() {
                    @Override
                    protected void updateItem(
                            Concierto item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(
                                    item.getNombreConcierto()
                            );
                        }
                    }
                });
    }

    // =========================
    // ASIGNAR
    // =========================
    @FXML
    public void On_asignarPresupuesto() {

        AnalisisFinanciero af =
                tablaFinanzas.getSelectionModel()
                        .getSelectedItem();

        Concierto concierto =
                comboConciertos.getValue();

        if (af == null || concierto == null) {

            mostrarError(
                    "Seleccione finanzas y concierto"
            );

            return;
        }

        conciertoService.asignarPresupuesto(
                concierto.getIdConcierto(),
                af.getIdAnalisisF()
        );

        mostrarExito(
                "Presupuesto asignado"
        );
    }

    // =========================
    // VOLVER
    // =========================
    @FXML
    public void On_volver() {

        try {

            sceneManager.showMenuFinanzas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // ALERTAS
    // =========================
    private void mostrarError(String msg) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Error");
        alert.setHeaderText("No se pudo continuar");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void mostrarExito(String msg) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Éxito");
        alert.setHeaderText("Operación realizada");
        alert.setContentText(msg);

        alert.showAndWait();
    }
}