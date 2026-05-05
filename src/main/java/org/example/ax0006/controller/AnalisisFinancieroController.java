package org.example.ax0006.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.ax0006.service.AnalisisFinancieroService;
import org.example.ax0006.manager.SceneManager;

import java.io.IOException;

public class AnalisisFinancieroController {

    // =========================
    // FXML
    // =========================
    @FXML private TextField fid_id;
    @FXML private TextField fid_presupuesto;
    @FXML private TextField fid_gasto;
    @FXML private TextField fid_precioBoleta;
    @FXML private Label lbl_resultado;

    // =========================
    // ATRIBUTOS
    // =========================
    private AnalisisFinancieroService service;
    private SceneManager sceneManager;

    // =========================
    // CONSTRUCTOR
    // =========================
    public AnalisisFinancieroController(AnalisisFinancieroService service, SceneManager sceneManager) {
        this.service = service;
        this.sceneManager = sceneManager;
    }

    // =========================
    // CREAR PRESUPUESTO
    // =========================
    @FXML
    public void On_crearPresupuesto() {

        try {
            int monto = Integer.parseInt(fid_presupuesto.getText());

            int id = service.crearPresupuesto(monto);

            if (id != 0) {
                mostrarExito("Presupuesto creado ID: " + id);
            } else {
                mostrarError("No se pudo crear");
            }

        } catch (Exception e) {
            mostrarError("Ingrese un monto válido");
        }
    }

    // =========================
    // ELIMINAR PRESUPUESTO
    // =========================
    @FXML
    public void On_eliminarPresupuesto() {

        try {
            int id = Integer.parseInt(fid_id.getText());

            service.eliminarPresupuesto(id);
            mostrarExito("Presupuesto eliminado");

        } catch (Exception e) {
            mostrarError("ID inválido");
        }
    }

    // =========================
    // REGISTRAR GASTO
    // =========================
    @FXML
    public void On_registrarGasto() {

        try {
            int id = Integer.parseInt(fid_id.getText());
            int gasto = Integer.parseInt(fid_gasto.getText());

            service.registrarGasto(id, gasto);
            mostrarExito("Gasto registrado");

        } catch (Exception e) {
            mostrarError("Datos inválidos");
        }
    }

    // =========================
    // ELIMINAR GASTO
    // =========================
    @FXML
    public void On_eliminarGasto() {

        try {
            int id = Integer.parseInt(fid_id.getText());
            int gasto = Integer.parseInt(fid_gasto.getText());

            service.eliminarGasto(id, gasto);
            mostrarExito("Gasto eliminado");

        } catch (Exception e) {
            mostrarError("Datos inválidos");
        }
    }

    // =========================
    // APROBAR PRESUPUESTO
    // =========================
    @FXML
    public void On_aprobarPresupuesto() {

        try {
            int id = Integer.parseInt(fid_id.getText());

            service.aprobarPresupuesto(id);
            mostrarExito("Presupuesto aprobado");

        } catch (Exception e) {
            mostrarError("No se pudo aprobar");
        }
    }

    // =========================
    // PRECIO BOLETA
    // =========================
    @FXML
    public void On_definirPrecio() {

        try {
            int id = Integer.parseInt(fid_id.getText());
            int precio = Integer.parseInt(fid_precioBoleta.getText());

            service.definirPrecioBoleta(id, precio);
            mostrarExito("Precio definido");

        } catch (Exception e) {
            mostrarError("Datos inválidos");
        }
    }

    // =========================
    // PUNTO DE EQUILIBRIO
    // =========================
    @FXML
    public void On_calcularEquilibrio() {

        try {
            int id = Integer.parseInt(fid_id.getText());

            int balance = service.obtenerBalance(id);

            lbl_resultado.setText("Balance: " + balance);

        } catch (Exception e) {
            mostrarError("Error al calcular");
        }
    }

    // =========================
    // VOLVER
    // =========================
    @FXML
    public void On_volver() throws IOException {
        sceneManager.showMenu();
    }

    // =========================
    // ALERTAS
    // =========================
    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("No se pudo continuar");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarExito(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText("Operación realizada");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}