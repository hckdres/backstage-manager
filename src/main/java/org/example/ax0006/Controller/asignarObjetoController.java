package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ax0006.Service.InventarioObjetoService;

public class asignarObjetoController {

    private InventarioObjetoService service;

    public asignarObjetoController(InventarioObjetoService service) {
        this.service = service;
    }

    @FXML private TextField ii_inventario;
    @FXML private TextField ii_objeto;
    @FXML private Label lbl_msg;

    @FXML
    void on_bt_asignar(ActionEvent event) {
        try {
            int inv = Integer.parseInt(ii_inventario.getText());
            int obj = Integer.parseInt(ii_objeto.getText());

            service.asignarObjetoAInventario(inv, obj);
            lbl_msg.setText("Asignado");

        } catch (Exception e) {
            lbl_msg.setText("Error en datos");
        }
    }
}