package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ax0006.Service.horarioService;

public class horarioController {

    private horarioService horarioservice;
    @FXML
    private Button bt_volver;

    public horarioController(horarioService horarioservice) {
          this.horarioservice = horarioservice;
    }

    @FXML private TextField ii_fecha;
    @FXML private TextField ii_horaInicio;
    @FXML private TextField ii_horaFin;
    @FXML private Label lbl_msg;

    @FXML
    void on_bt_crear(ActionEvent event) {

        int id = horarioservice.crearHorario(
                ii_fecha.getText(),
                ii_horaInicio.getText(),
                ii_horaFin.getText()
        );

        if (id == -1) {
            lbl_msg.setText("error en datos");
        } else {
            lbl_msg.setText("Horario creado con ID: " + id);
        }

        ii_fecha.clear();
        ii_horaInicio.clear();
        ii_horaFin.clear();
    }

    @FXML
    void on_bt_volver(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/ax0006/menu.fxml")
            );

            menuController controller = new menuController(null, null);

            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) bt_volver.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}