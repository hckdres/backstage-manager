package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ax0006.Service.crearTipoObjetoService;

public class crearTipoObjetoController {

    private crearTipoObjetoService service;

    public crearTipoObjetoController(crearTipoObjetoService service) {
        this.service = service;
    }

    @FXML private TextField ii_nombre;
    @FXML private Label lbl_msg;

    @FXML
    void on_bt_crear(ActionEvent event) {
        String nombre = ii_nombre.getText();

        if (nombre.isEmpty()) {
            lbl_msg.setText("Nombre vacío");
            return;
        }

        service.crearTipoObjeto(nombre);
        lbl_msg.setText("Tipo creado");
        ii_nombre.clear();
    }
}