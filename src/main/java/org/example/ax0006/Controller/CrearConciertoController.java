package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.ax0006.Entity.Concierto;
import org.example.ax0006.Entity.Horario;
import org.example.ax0006.Manager.SceneManager;
import org.example.ax0006.Manager.SesionManager;
import org.example.ax0006.Service.ConciertoService;

import java.io.IOException;
import java.time.LocalTime;

public class CrearConciertoController {

    private SesionManager sesion;
    private ConciertoService conciertoService;
    private SceneManager sceneManager;

    public CrearConciertoController(SesionManager sesion, ConciertoService conciertoService, SceneManager sceneManager){
        this.sesion = sesion;
        this.conciertoService = conciertoService;
        this.sceneManager = sceneManager;
    }

    @FXML
    private DatePicker fid_fecha;

    @FXML
    private TextField fid_horaInicio;

    @FXML
    private TextField fid_horaFin;

    @FXML
    private TextField fid_aforo;

    @FXML
    void On_crearConcierto(ActionEvent event) {

        try {
            // Fecha
            java.time.LocalDate fecha = fid_fecha.getValue();

            // Horas

            LocalTime horaInicio = LocalTime.parse(fid_horaInicio.getText());
            LocalTime horaFin = LocalTime.parse(fid_horaFin.getText());

            // Aforo
            int aforo = Integer.parseInt(fid_aforo.getText());

            // Horario
            Horario horario = new Horario();
            horario.setFecha(fecha);
            horario.setHoraInicio(horaInicio);
            horario.setHoraFin(horaFin);

            // Concierto
            Concierto concierto = new Concierto();
            concierto.setHorario(horario);
            concierto.setAforo(aforo);
            concierto.setArtista(sesion.getUsuarioActual());
            concierto.setProgramado(false); // importante

            conciertoService.crearConcierto(concierto);

            exitoConcierto();

        } catch (Exception e) {
            e.printStackTrace();
            alertaConcierto("llene todos los campos con los formatos requeridos");
        }
    }

    @FXML
    void On_volver(ActionEvent event) throws IOException {
        sceneManager.showMenu();
    }


    /*METODO PARA HACER QUE SALGA UNA VENTANA DE ERROR, CUANDO HAY ALGUN ERROR EN EL SIGN UP*/
    void alertaConcierto(String CausaError){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al crear el concierto");
        alert.setHeaderText("El concierto no se pudo crear");
        alert.setContentText(CausaError);

        alert.showAndWait(); // Esto abre el POP UP
    }

    /*METODO PARA HACER QUE SALGA UNA VENTANA DE EXIITO, CUANDO SE CREA LA CUENTA CORRECTAMENTE*/
    void exitoConcierto (){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("El concierto fue creado");
        alert.setHeaderText("El la solicitud del concierto fue creada correctamente");
        alert.setContentText("La solicitud sera revisada para que el concierto sea programado");
        alert.showAndWait(); // Esto abre el POP UP
    }
}
