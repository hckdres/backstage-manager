package org.example.ax0006.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.InventarioService;
import org.example.ax0006.Service.ObjetoService;

import java.util.ArrayList;
import java.util.List;

public class crearInventarioController {
    private final InventarioService inventarioService;
    private final ObjetoService objetoService; // Necesitas este servicio
    private final SceneManager sceneManager;
    private final SesionManager sesionManager;

    @FXML private DatePicker dp_fechaInicio;
    @FXML private DatePicker dp_fechaFin;
    @FXML private TextField tf_horaInicio;
    @FXML private TextField tf_horaFin;
    @FXML private TextField tf_idConcierto;
    @FXML private ComboBox<String> cb_objetos;
    @FXML private ListView<String> lv_objetos_seleccionados;

    private ObservableList<String> listaVisualObjetos = FXCollections.observableArrayList();
    private List<Integer> listaIdsParaGuardar = new ArrayList<>();

    public crearInventarioController(InventarioService inventarioService, ObjetoService objetoService, SceneManager sceneManager, SesionManager sesionManager) {
        this.inventarioService = inventarioService;
        this.objetoService = objetoService;
        this.sceneManager = sceneManager;
        this.sesionManager = sesionManager;
    }

    @FXML
    public void initialize() {
        Concierto c = sesionManager.getConciertoTemporal();
        if (c != null) {
            tf_idConcierto.setText(String.valueOf(c.getIdConcierto()));
            if (c.getHorario() != null) {
                dp_fechaInicio.setValue(c.getHorario().getFechaInicio());
                dp_fechaFin.setValue(c.getHorario().getFechaFin());
                tf_horaInicio.setText(c.getHorario().getHoraInicio().toString());
                tf_horaFin.setText(c.getHorario().getHoraFin().toString());
            }
        }

        List<String> objetosDB = objetoService.obtenerListaObjetosFormateada();
        cb_objetos.setItems(FXCollections.observableArrayList(objetosDB));
        lv_objetos_seleccionados.setItems(listaVisualObjetos);
    }

    @FXML
    void on_bt_agregar_lista() {
        String seleccionado = cb_objetos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !listaVisualObjetos.contains(seleccionado)) {
            listaVisualObjetos.add(seleccionado);
            // Extraer el ID (asumiendo formato "ID - Nombre")
            int id = Integer.parseInt(seleccionado.split(" - ")[0]);
            listaIdsParaGuardar.add(id);
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
    void on_bt_crear() {
        try {
            Concierto c = sesionManager.getConciertoTemporal();
            int idConcierto = Integer.parseInt(tf_idConcierto.getText());
            int idHorario = (c != null && c.getHorario() != null) ? c.getHorario().getIdHorario() : 0;

            inventarioService.crearDocumentoInventario(idConcierto, idHorario, listaIdsParaGuardar);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Inventario creado y objetos vinculados exitosamente.");
            alert.showAndWait();

            sesionManager.setConciertoTemporal(null);
            sceneManager.showMenuConcierto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void on_bt_volver() throws Exception {
        sesionManager.setConciertoTemporal(null);
        sceneManager.showMenuConcierto();
    }
}