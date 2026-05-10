package org.example.ax0006.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.ax0006.Entity.Horario;
import org.example.ax0006.Entity.Objeto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Repository.HorarioRepository;
import org.example.ax0006.Service.InventarioObjetoService;
import org.example.ax0006.Service.InventarioService;
import org.example.ax0006.Service.ObjetoService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoController {
    private final InventarioService inventarioService;
    private final ObjetoService objetoService;
    private final HorarioRepository horarioRepository;
    private final SceneManager sceneManager;
    private final SesionManager sesionManager;
    private final InventarioObjetoService inventarioObjetoService;

    @FXML private VBox panel_lista;
    @FXML private VBox vb_lista_mantenimientos;
    @FXML private HBox panel_creacion;
    @FXML private DatePicker dp_fechaInicio;
    @FXML private DatePicker dp_fechaFin;
    @FXML private TextField tf_horaInicio;
    @FXML private TextField tf_horaFin;
    @FXML private TextField tf_idConcierto;
    @FXML private ComboBox<String> cb_objetos;
    @FXML private ListView<String> lv_objetos_seleccionados;

    private ObservableList<String> listaVisualObjetos = FXCollections.observableArrayList();
    private List<Integer> listaIdsParaGuardar = new ArrayList<>();

    public MantenimientoController(InventarioService inventarioService, ObjetoService objetoService, HorarioRepository horarioRepository, SceneManager sceneManager, SesionManager sesionManager, InventarioObjetoService inventarioObjetoService) {
        this.inventarioService = inventarioService;
        this.objetoService = objetoService;
        this.horarioRepository = horarioRepository;
        this.sceneManager = sceneManager;
        this.sesionManager = sesionManager;
        this.inventarioObjetoService = inventarioObjetoService;
    }

    @FXML
    public void initialize() {
        if(tf_idConcierto != null) tf_idConcierto.setText("mantenimiento");
        List<String> objetosDB = objetoService.obtenerListaObjetosFormateada();
        cb_objetos.setItems(FXCollections.observableArrayList(objetosDB));
        lv_objetos_seleccionados.setItems(listaVisualObjetos);
        mostrarPanelCreacion(false);
        cargarMantenimientosActivos();
    }

    private void cargarMantenimientosActivos() {
        vb_lista_mantenimientos.getChildren().clear();
        List<Integer> libres = inventarioService.obtenerInventariosSinConcierto();

        if (libres.isEmpty()) {
            Label lblVacio = new Label("No hay equipos en mantenimiento actualmente.");
            lblVacio.setStyle("-fx-text-fill: #6b7280; -fx-font-style: italic;");
            vb_lista_mantenimientos.getChildren().add(lblVacio);
            return;
        }

        for (Integer idInv : libres) {
            int idHorario = inventarioService.obtenerIdHorarioPorInventario(idInv);
            Horario h = horarioRepository.obtenerHorarioPorId(idHorario);
            List<Objeto> objetos = inventarioService.obtenerObjetosPorInventario(idInv);

            String textoFechas = (h != null) ?
                    "Mantenimiento: " + h.getFechaInicio() + " [" + h.getHoraInicio() + "] hasta " + h.getFechaFin() + " [" + h.getHoraFin() + "]" :
                    "Inventario #" + idInv;

            StringBuilder textoObjetos = new StringBuilder("Elementos: ");
            List<Integer> idsObjetosParaEliminar = new ArrayList<>();
            for (int i = 0; i < objetos.size(); i++) {
                textoObjetos.append(objetos.get(i).getTipoObjeto().getTipo());
                if (i < objetos.size() - 1) textoObjetos.append(", ");
                idsObjetosParaEliminar.add(objetos.get(i).getIdObjeto());
            }

            HBox fila = new HBox(15);
            fila.setStyle("-fx-background-color: #f9fafb; -fx-padding: 15; -fx-background-radius: 8; -fx-border-color: #e5e7eb; -fx-border-radius: 8;");
            fila.setAlignment(Pos.CENTER_LEFT);

            VBox info = new VBox(5);
            Label lblTitulo = new Label(textoFechas);
            lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1f2937;");
            Label lblDetalle = new Label(textoObjetos.toString());
            lblDetalle.setStyle("-fx-text-fill: #4b5563;");
            lblDetalle.setWrapText(true);

            info.getChildren().addAll(lblTitulo, lblDetalle);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button btnEliminar = new Button("Terminar");
            btnEliminar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
            btnEliminar.setOnAction(e -> {
                inventarioService.eliminarDocumentoInventario(idInv, 0, idHorario, idsObjetosParaEliminar);
                cargarMantenimientosActivos();
            });

            fila.getChildren().addAll(info, btnEliminar);
            vb_lista_mantenimientos.getChildren().add(fila);
        }
    }

    @FXML
    void on_bt_crear() {
        try {
            if (dp_fechaInicio.getValue() == null || dp_fechaFin.getValue() == null ||
                    tf_horaInicio.getText().isEmpty() || tf_horaFin.getText().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor, completa todas las fechas y horas.");
                return;
            }

            if (listaIdsParaGuardar.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Lista vacía", "Debes agregar al menos un objeto.");
                return;
            }

            Horario nuevoHorario = new Horario();
            nuevoHorario.setFechaInicio(dp_fechaInicio.getValue());
            nuevoHorario.setFechaFin(dp_fechaFin.getValue());
            nuevoHorario.setHoraInicio(LocalTime.parse(tf_horaInicio.getText()));
            nuevoHorario.setHoraFin(LocalTime.parse(tf_horaFin.getText()));

            List<String> conflictos = new ArrayList<>();
            for (int i = 0; i < listaIdsParaGuardar.size(); i++) {
                int idObjeto = listaIdsParaGuardar.get(i);
                if (inventarioObjetoService.objetoEnUsoEnRango(idObjeto, nuevoHorario)) {
                    conflictos.add(listaVisualObjetos.get(i));
                }
            }

            if (!conflictos.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Conflicto de Horario",
                        "Los siguientes objetos ya están ocupados en el rango seleccionado:\n\n" + String.join("\n", conflictos));
                return;
            }

            int idHorarioGenerado = horarioRepository.guardar(nuevoHorario);

            if (idHorarioGenerado != -1) {
                int idInventario = inventarioService.crearDocumentoInventario(0, idHorarioGenerado, listaIdsParaGuardar);

                if (idInventario != -1) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El inventario de mantenimiento se ha creado correctamente.");
                    on_bt_cancelar_creacion();
                    cargarMantenimientosActivos();
                } else {
                    System.out.println("no se creó xd");
                }
            } else {
                System.out.println("no se guardó horario xd");
            }

        } catch (java.time.format.DateTimeParseException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Formato", "Usa el formato HH:mm (ej: 14:30).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void on_bt_agregar_lista() {
        String seleccionado = cb_objetos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !listaVisualObjetos.contains(seleccionado)) {
            try {
                if (dp_fechaInicio.getValue() != null && !tf_horaInicio.getText().isEmpty() &&
                        dp_fechaFin.getValue() != null && !tf_horaFin.getText().isEmpty()) {

                    Horario hTemp = new Horario();
                    hTemp.setFechaInicio(dp_fechaInicio.getValue());
                    hTemp.setFechaFin(dp_fechaFin.getValue());
                    hTemp.setHoraInicio(LocalTime.parse(tf_horaInicio.getText()));
                    hTemp.setHoraFin(LocalTime.parse(tf_horaFin.getText()));

                    int id = Integer.parseInt(seleccionado.split(" - ")[0]);
                    if (inventarioObjetoService.objetoEnUsoEnRango(id, hTemp)) {
                        mostrarAlerta(Alert.AlertType.WARNING, "Objeto Ocupado",
                                "Este objeto ya está en uso durante las fechas seleccionadas.");
                        return;
                    }
                }

                listaVisualObjetos.add(seleccionado);
                listaIdsParaGuardar.add(Integer.parseInt(seleccionado.split(" - ")[0]));

            } catch (Exception e) {
                listaVisualObjetos.add(seleccionado);
                listaIdsParaGuardar.add(Integer.parseInt(seleccionado.split(" - ")[0]));
            }
        }
    }

    @FXML
    void on_bt_quitar_lista() {
        int index = lv_objetos_seleccionados.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            listaVisualObjetos.remove(index);
            listaIdsParaGuardar.remove(index);
        }
    }

    @FXML
    void on_bt_abrir_creacion() { mostrarPanelCreacion(true); }

    @FXML
    void on_bt_cancelar_creacion() {
        tf_horaInicio.clear(); tf_horaFin.clear();
        dp_fechaInicio.setValue(null); dp_fechaFin.setValue(null);
        listaVisualObjetos.clear(); listaIdsParaGuardar.clear();
        mostrarPanelCreacion(false);
    }

    private void mostrarPanelCreacion(boolean mostrar) {
        panel_creacion.setVisible(mostrar);
        panel_creacion.setManaged(mostrar);
        panel_lista.setVisible(!mostrar);
        panel_lista.setManaged(!mostrar);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void on_bt_volver() throws Exception {
        sesionManager.setConciertoTemporal(null);
        sceneManager.showMenu();
    }
}