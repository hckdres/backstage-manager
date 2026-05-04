package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.example.ax0006.Entity.Actividad;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ActividadService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActividadController {
    private final SceneManager sceneManager;
    private final SesionManager sesion;
    private final ActividadService actividadService;
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private List<Actividad> actividades = new ArrayList<>();
    private Actividad actividadSeleccionada;
    private String filtroActual = "TODO";

    public ActividadController(SceneManager sceneManager, SesionManager sesion, ActividadService actividadService) {
        this.sceneManager = sceneManager;
        this.sesion = sesion;
        this.actividadService = actividadService;
    }

    @FXML private Label fid_lbl_total_actividad;
    @FXML private Button fid_bt_filtro_todo;
    @FXML private Button fid_bt_filtro_accesos;
    @FXML private Button fid_bt_filtro_alertas;
    @FXML private Button fid_bt_filtro_mensajes;
    @FXML private Button fid_bt_filtro_solicitudes;

    @FXML private Button fid_bt_actividad_1;
    @FXML private Button fid_bt_actividad_2;
    @FXML private Button fid_bt_actividad_3;
    @FXML private Button fid_bt_actividad_4;
    @FXML private Button fid_bt_actividad_5;

    @FXML private Label fid_lbl_tipo_actividad;
    @FXML private Label fid_lbl_estado_actividad;
    @FXML private Label fid_lbl_usuario_actividad;
    @FXML private Label fid_lbl_fecha_actividad;
    @FXML private Label fid_lbl_modulo_actividad;
    @FXML private Label fid_lbl_origen_actividad;
    @FXML private TextArea fid_txt_detalle_actividad;

    @FXML
    public void initialize() {
        if (fid_txt_detalle_actividad != null) {
            fid_txt_detalle_actividad.setEditable(false);
            fid_txt_detalle_actividad.setWrapText(true);
        }

        cargarActividades("TODO");
    }

    @FXML
    void On_filtro_todo(ActionEvent event) {
        cargarActividades("TODO");
    }

    @FXML
    void On_filtro_accesos(ActionEvent event) {
        cargarActividades("ACCESO");
    }

    @FXML
    void On_filtro_alertas(ActionEvent event) {
        cargarActividades("ALERTA");
    }

    @FXML
    void On_filtro_mensajes(ActionEvent event) {
        cargarActividades("MENSAJE");
    }

    @FXML
    void On_filtro_solicitudes(ActionEvent event) {
        cargarActividades("SOLICITUD");
    }

    @FXML
    void On_actividad_1(ActionEvent event) {
        seleccionarActividad(0);
    }

    @FXML
    void On_actividad_2(ActionEvent event) {
        seleccionarActividad(1);
    }

    @FXML
    void On_actividad_3(ActionEvent event) {
        seleccionarActividad(2);
    }

    @FXML
    void On_actividad_4(ActionEvent event) {
        seleccionarActividad(3);
    }

    @FXML
    void On_actividad_5(ActionEvent event) {
        seleccionarActividad(4);
    }

    @FXML
    void On_marcar_revisado(ActionEvent event) {
        if (actividadSeleccionada == null) return;

        actividadService.marcarRevisado(
                actividadSeleccionada.getIdActividad(),
                sesion.getUsuarioActual()
        );

        cargarActividades(filtroActual);
    }

    @FXML
    void On_volver_actividad(ActionEvent event) throws IOException {
        sceneManager.showMenu();
    }

    private void cargarActividades(String filtro) {
        filtroActual = filtro;

        actividades = actividadService.listarParaUsuario(
                sesion.getUsuarioActual(),
                filtro
        );

        int pendientes = actividadService.contarPendientes(sesion.getUsuarioActual());

        fid_lbl_total_actividad.setText(
                pendientes == 1 ? "1 nuevo" : pendientes + " nuevos"
        );

        pintarBotones();
        resaltarFiltro(filtro);

        if (actividades.isEmpty()) {
            limpiarDetalle();
        } else {
            seleccionarActividad(0);
        }
    }

    private void pintarBotones() {
        Button[] botones = obtenerBotonesActividad();

        for (int i = 0; i < botones.length; i++) {
            Button boton = botones[i];

            if (i < actividades.size()) {
                Actividad actividad = actividades.get(i);

                boton.setText(crearTextoBoton(actividad));
                boton.setVisible(true);
                boton.setManaged(true);

                aplicarEstiloActividad(boton, actividad.getTipo());
            } else {
                boton.setVisible(false);
                boton.setManaged(false);
            }
        }
    }

    private String crearTextoBoton(Actividad actividad) {
        String usuario = actividad.getNombreUsuarioActor() == null
                ? "Sistema"
                : actividad.getNombreUsuarioActor();

        String fecha = actividad.getFechaHora() == null
                ? "Sin fecha"
                : actividad.getFechaHora().format(formatoFecha);

        return "● " + actividad.getModulo() + " - " + usuario + " - " + fecha;
    }

    private void seleccionarActividad(int indice) {
        if (indice < 0 || indice >= actividades.size()) return;

        actividadSeleccionada = actividades.get(indice);

        fid_lbl_tipo_actividad.setText(actividadSeleccionada.getModulo());

        fid_lbl_estado_actividad.setText(
                actividadSeleccionada.isRevisado() ? "Revisado" : "Nuevo"
        );

        fid_lbl_usuario_actividad.setText(
                actividadSeleccionada.getNombreUsuarioActor() == null
                        ? "Sistema"
                        : actividadSeleccionada.getNombreUsuarioActor()
        );

        fid_lbl_fecha_actividad.setText(
                actividadSeleccionada.getFechaHora() == null
                        ? "Sin fecha"
                        : actividadSeleccionada.getFechaHora().format(formatoFecha)
        );

        fid_lbl_modulo_actividad.setText("Módulo: " + actividadSeleccionada.getModulo());
        fid_lbl_origen_actividad.setText("Origen: " + actividadSeleccionada.getOrigen());
        fid_txt_detalle_actividad.setText(actividadSeleccionada.getDescripcion());
    }

    private void limpiarDetalle() {
        actividadSeleccionada = null;

        fid_lbl_tipo_actividad.setText("Sin actividad");
        fid_lbl_estado_actividad.setText("Revisado");
        fid_lbl_usuario_actividad.setText("-");
        fid_lbl_fecha_actividad.setText("-");
        fid_lbl_modulo_actividad.setText("Módulo: -");
        fid_lbl_origen_actividad.setText("Origen: -");
        fid_txt_detalle_actividad.setText("No hay actividad para este filtro.");
    }

    private Button[] obtenerBotonesActividad() {
        return new Button[]{
                fid_bt_actividad_1,
                fid_bt_actividad_2,
                fid_bt_actividad_3,
                fid_bt_actividad_4,
                fid_bt_actividad_5
        };
    }

    private void resaltarFiltro(String filtro) {
        Button[] filtros = {
                fid_bt_filtro_todo,
                fid_bt_filtro_accesos,
                fid_bt_filtro_alertas,
                fid_bt_filtro_mensajes,
                fid_bt_filtro_solicitudes
        };

        for (Button boton : filtros) {
            boton.setStyle(
                    "-fx-background-color: #f3f4f6;" +
                            "-fx-text-fill: #374151;" +
                            "-fx-font-weight: bold;" +
                            "-fx-background-radius: 20;" +
                            "-fx-border-color: #d1d5db;" +
                            "-fx-border-radius: 20;"
            );
        }

        Button activo = switch (filtro) {
            case "ACCESO" -> fid_bt_filtro_accesos;
            case "ALERTA" -> fid_bt_filtro_alertas;
            case "MENSAJE" -> fid_bt_filtro_mensajes;
            case "SOLICITUD" -> fid_bt_filtro_solicitudes;
            default -> fid_bt_filtro_todo;
        };

        activo.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 20;"
        );
    }

    private void aplicarEstiloActividad(Button boton, String tipo) {
        String colorTexto = "#1d4ed8";
        String colorBorde = "#bfdbfe";

        if ("ALERTA".equalsIgnoreCase(tipo)) {
            colorTexto = "#c2410c";
            colorBorde = "#fed7aa";
        } else if ("MENSAJE".equalsIgnoreCase(tipo)) {
            colorTexto = "#6d28d9";
            colorBorde = "#ddd6fe";
        } else if ("SOLICITUD".equalsIgnoreCase(tipo)) {
            colorTexto = "#047857";
            colorBorde = "#bbf7d0";
        }

        boton.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-text-fill: " + colorTexto + ";" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: " + colorBorde + ";" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-width: 1.2;"
        );
    }
}