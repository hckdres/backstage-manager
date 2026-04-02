package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.ax0006.Service.InventarioService;

public class crearInventarioController {

    private InventarioService service;

    public crearInventarioController(InventarioService service) {
        this.service = service;
    }

    @FXML private Label lbl_msg;

    @FXML
    void on_bt_crear(ActionEvent event) {
        service.crearInventario();
        lbl_msg.setText("Inventario creado");
    }
}