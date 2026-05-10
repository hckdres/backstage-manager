package org.example.ax0006.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Entity.Objeto;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.InventarioService;
import org.example.ax0006.Service.ObjetoService;

import java.util.ArrayList;
import java.util.List;

public class crearInventarioController {
    private final InventarioService inventarioService;
    private final ObjetoService objetoService; 
    private final SceneManager sceneManager;
    private final SesionManager sesionManager;

    @FXML private DatePicker dp_fechaInicio;
    @FXML private DatePicker dp_fechaFin;
    @FXML private TextField tf_horaInicio;
    @FXML private TextField tf_horaFin;
    @FXML private TextField tf_idConcierto;
    @FXML private ComboBox<String> cb_objetos;
    @FXML private ListView<String> lv_objetos_seleccionados;
    @FXML private VBox panel_agregar;
    @FXML private VBox panel_eliminar;
    @FXML private Button btn_confirmar_todo;

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
            List<String> inventarioExistente = inventarioService.obtenerObjetosPorConcierto(c.getIdConcierto());

            if (!inventarioExistente.isEmpty()) {
                mostrarPanelEliminar(true);
            }

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

    private void mostrarPanelEliminar(boolean mostrarEliminar) {
        panel_eliminar.setVisible(mostrarEliminar);
        panel_eliminar.setManaged(mostrarEliminar);

        panel_agregar.setVisible(!mostrarEliminar);
        panel_agregar.setManaged(!mostrarEliminar);
    }

    @FXML
    void on_bt_eliminar_existente() {
        Concierto c = sesionManager.getConciertoTemporal();
        if (c == null) return;

        try {
            int idConcierto = c.getIdConcierto();
            int idHorario = c.getHorario().getIdHorario();
            int idInventario = inventarioService.obtenerDocumentoInventarioPorConcierto(idConcierto);
            List<Objeto> inventarioExistente = inventarioService.obtenerObjetoObjetosPorConcierto(idConcierto);
            List<Integer> idsObjetosActuales = new ArrayList<Integer>();

            for(Objeto objeto : inventarioExistente){
                int idObjeto = objeto.getIdObjeto();
                idsObjetosActuales.add(idObjeto);
            }

            inventarioService.eliminarDocumentoInventario(idInventario, idConcierto, idHorario, idsObjetosActuales);
            listaVisualObjetos.clear();
            listaIdsParaGuardar.clear();

            panel_eliminar.setVisible(false);
            panel_eliminar.setManaged(false);
            panel_agregar.setVisible(true);
            panel_agregar.setManaged(true);

            btn_confirmar_todo.setDisable(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void on_bt_agregar_lista() {
        String seleccionado = cb_objetos.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !listaVisualObjetos.contains(seleccionado)) {
            listaVisualObjetos.add(seleccionado);
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

            if (!inventarioService.obtenerObjetosPorConcierto(c.getIdConcierto()).isEmpty()) {
                System.out.println("Error: Intento de duplicación de inventario bloqueado.");
                return;
            }
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