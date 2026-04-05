package org.example.ax0006.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.ax0006.Service.InventarioObjetoService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class asignarObjetoController {

    public Button bt_asignar;
    private InventarioObjetoService service;
    @FXML
    private Button bt_volver;

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

            int resultado = service.asignarObjetoAInventario(inv, obj);

            if (resultado == -1) {
                lbl_msg.setText("El objeto ya está ocupado");
            } else {
                lbl_msg.setText("Asignado correctamente )");
            }

        } catch (NumberFormatException e) {
            lbl_msg.setText("Error: ingresa números válidos");
        } catch (Exception e) {
            lbl_msg.setText("Error inesperado");
        }
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