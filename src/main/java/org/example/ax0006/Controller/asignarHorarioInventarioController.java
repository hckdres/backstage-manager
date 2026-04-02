package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ax0006.Service.InventarioHorarioService;

public class asignarHorarioInventarioController {

    private InventarioHorarioService service;

    public asignarHorarioInventarioController(InventarioHorarioService service) {
        this.service = service;
    }

    @FXML private TextField ii_inventario;
    @FXML private TextField ii_horario;
    @FXML private Label lbl_msg;

    @FXML
    void on_bt_asignar(ActionEvent event) {
        try {
            int inv = Integer.parseInt(ii_inventario.getText());
            int hor = Integer.parseInt(ii_horario.getText());

            service.asignarInventarioAHorario(inv, hor);
            lbl_msg.setText("Asignado");

        } catch (Exception e) {
            lbl_msg.setText("Error en datos");
        }
    }
}