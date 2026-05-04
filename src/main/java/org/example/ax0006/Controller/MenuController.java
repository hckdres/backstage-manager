package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ConciertoService;
import org.example.ax0006.Service.ActividadService;

import java.io.IOException;

public class MenuController {

    private SceneManager sceneManager;
    private SesionManager sesion;
    private ConciertoService conciertoService;
    private ActividadService actividadService;

    public MenuController() {
    }

    public MenuController(SceneManager sceneManager, SesionManager sesion, ConciertoService conciertoService, ActividadService actividadService) {
        this.sceneManager = sceneManager;
        this.sesion = sesion;
        this.conciertoService = conciertoService;
        this.actividadService = actividadService;
    }

    @FXML
    private Label fid_Bienvenido;

    @FXML
    private Button fid_bt_volver;

    @FXML
    private Button fid_Menu_Conciertos;

    @FXML
    private Label fid_lbl_contador_bandeja;

    @FXML
    public void initialize() {
        if (sesion != null && sesion.getUsuarioActual() != null && fid_Bienvenido != null) {
            fid_Bienvenido.setText("Bienvenido " + sesion.getUsuarioActual().getNombre());
        }

        actualizarContadorBandeja();
    }

    @FXML
    void On_btvolver(ActionEvent event) throws IOException {
        if (actividadService != null && sesion != null) {
            actividadService.registrarLogout(sesion.getUsuarioActual());
        }

        sesion.cerrarSesion();
        sceneManager.showLogin();
    }

    @FXML
    void On_bandeja(ActionEvent event) throws IOException {
        sceneManager.showActividad();
    }

    private void actualizarContadorBandeja() {
        if (fid_lbl_contador_bandeja == null || actividadService == null || sesion == null || sesion.getUsuarioActual() == null) {
            return;
        }

        int pendientes = actividadService.contarPendientes(sesion.getUsuarioActual());

        fid_lbl_contador_bandeja.setText(String.valueOf(pendientes));
        fid_lbl_contador_bandeja.setVisible(pendientes > 0);
        fid_lbl_contador_bandeja.setManaged(pendientes > 0);
    }

    @FXML
    void On_admin(ActionEvent event) throws IOException {
        sceneManager.showAdminUsuarios();
    }

    @FXML
    void On_Perfil(ActionEvent event) {
        try {
            sceneManager.showProfile();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana de perfil");
            alert.setContentText("Ocurrió un problema al cargar la vista.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void On_Menu_Conciertos(ActionEvent event) throws IOException {
        sceneManager.showMenuConcierto();
    }
}