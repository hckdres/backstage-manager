package org.example.ax0006.Controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Entity.Nomina;
import org.example.ax0006.Entity.Usuario;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ConciertoService;
import org.example.ax0006.Service.NominaService;
import org.example.ax0006.Service.StaffService;

import java.util.List;

public class NominaController {

    private final SceneManager sceneManager;
    private final SesionManager sesion;
    private final ConciertoService conciertoService;
    private final NominaService nominaService;
    private final StaffService staffService;

    private ComboBox<Concierto> comboEvento;
    private TableView<Nomina> tablaNominas;
    private Label lblTotalGeneral;

    public NominaController(SceneManager sceneManager, SesionManager sesion,
                            ConciertoService conciertoService, NominaService nominaService,
                            StaffService staffService) {
        this.sceneManager = sceneManager;
        this.sesion = sesion;
        this.conciertoService = conciertoService;
        this.nominaService = nominaService;
        this.staffService = staffService;
    }

    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f4f6f8;");

        // Barra superior
        HBox topBar = new HBox(10);
        topBar.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 2);");
        Label title = new Label("Liquidación de Horas");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        topBar.getChildren().add(title);
        root.setTop(topBar);

        // Contenido central
        VBox center = new VBox(20);
        center.setPadding(new Insets(30));
        center.setStyle("-fx-background-color: #f4f6f8;");

        Label sectionTitle = new Label("Liquidación por Evento");
        sectionTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        Separator separator = new Separator();

        HBox selectionBox = new HBox(10);
        selectionBox.setAlignment(Pos.CENTER_LEFT);
        Label eventLabel = new Label("Seleccione evento:");
        eventLabel.setStyle("-fx-font-weight: bold;");
        comboEvento = new ComboBox<>();
        comboEvento.setPrefWidth(300);
        selectionBox.getChildren().addAll(eventLabel, comboEvento);

        // Tabla
        tablaNominas = new TableView<>();
        tablaNominas.setPrefHeight(500);

        TableColumn<Nomina, String> colTrabajador = new TableColumn<>("Trabajador");
        colTrabajador.setPrefWidth(200);
        TableColumn<Nomina, Number> colHoras = new TableColumn<>("Horas trabajadas");
        colHoras.setPrefWidth(150);
        TableColumn<Nomina, Number> colTarifa = new TableColumn<>("Tarifa por hora");
        colTarifa.setPrefWidth(150);
        TableColumn<Nomina, Number> colExtra = new TableColumn<>("Horas extra");
        colExtra.setPrefWidth(150);
        TableColumn<Nomina, Number> colTotal = new TableColumn<>("Total");
        colTotal.setPrefWidth(150);
        TableColumn<Nomina, String> colEstado = new TableColumn<>("Estado");
        colEstado.setPrefWidth(150);

        tablaNominas.getColumns().addAll(colTrabajador, colHoras, colTarifa, colExtra, colTotal, colEstado);

        // Configurar columnas
        colTrabajador.setCellValueFactory(cellData -> {
            int idUsuario = cellData.getValue().getIdUsuario();
            Usuario u = staffService.listarEmpleados().stream()
                    .filter(emp -> emp.getIdUsuario() == idUsuario).findFirst().orElse(null);
            return new SimpleStringProperty(u != null ? u.getNombre() : "Desconocido");
        });
        colHoras.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getHorasTrabajadas()));
        colTarifa.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTarifaPorHora()));
        colExtra.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getHorasExtra()));
        colTotal.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotal()));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isPagado() ? "Pagado" : "Pendiente"));

        // Edición de horas extra
        colExtra.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.DoubleStringConverter()));
        colExtra.setOnEditCommit(event -> {
            Nomina n = event.getRowValue();
            double nuevasHorasExtra = event.getNewValue();
            nominaService.actualizarHorasExtra(n.getIdNomina(), nuevasHorasExtra);
            cargarNominas(comboEvento.getValue().getIdConcierto());
        });

        // Total general y botón reporte
        HBox totalBox = new HBox(15);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label("Total general:");
        totalLabel.setStyle("-fx-font-weight: bold;");
        lblTotalGeneral = new Label("$0");
        lblTotalGeneral.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        Button btnReporte = new Button("Generar Reporte");
        btnReporte.setStyle("-fx-background-color: #27ae60; -fx-background-radius: 8; -fx-text-fill: white; -fx-font-weight: bold;");
        btnReporte.setOnAction(e -> {
            Concierto c = comboEvento.getValue();
            if (c != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Reporte");
                alert.setHeaderText("Generar reporte para: " + c.getNombreConcierto());
                alert.setContentText("Aquí se mostrará la pantalla de exportación.");
                alert.showAndWait();
            }
        });
        totalBox.getChildren().addAll(totalLabel, lblTotalGeneral, btnReporte);

        center.getChildren().addAll(sectionTitle, separator, selectionBox, tablaNominas, totalBox);
        root.setCenter(center);

        // Cargar eventos programados
        List<Concierto> conciertos = conciertoService.obtenerConciertosSolos().stream()
                .filter(Concierto::isProgramado)
                .toList();
        comboEvento.setItems(FXCollections.observableArrayList(conciertos));
        comboEvento.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Concierto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreConcierto());
            }
        });
        comboEvento.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Concierto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreConcierto());
            }
        });
        comboEvento.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                nominaService.generarNominaParaConcierto(newVal.getIdConcierto());
                cargarNominas(newVal.getIdConcierto());
            }
        });

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.show();
    }

    private void cargarNominas(int idConcierto) {
        List<Nomina> nominas = nominaService.obtenerNominasPorConcierto(idConcierto);
        tablaNominas.setItems(FXCollections.observableArrayList(nominas));
        double total = nominaService.calcularTotalGeneral(idConcierto);
        lblTotalGeneral.setText(String.format("$%,.0f", total));
    }
}