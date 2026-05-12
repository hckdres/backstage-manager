package org.example.ax0006.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.example.ax0006.entity.Nomina;
import org.example.ax0006.entity.Usuario;

import org.example.ax0006.manager.SceneManager;
import org.example.ax0006.manager.SesionManager;

import org.example.ax0006.service.StaffService;

import java.io.IOException;

public class DetalleNominaController {

    @FXML private Label lblNombre;
    @FXML private Label lblRol;
    @FXML private Label lblHoras;
    @FXML private Label lblExtra;
    @FXML private Label lblTarifa;
    @FXML private Label lblTotal;
    @FXML private Label lblEstado;

    private SceneManager sceneManager;
    private SesionManager sesion;
    private StaffService staffService;

    public DetalleNominaController() {}

    public DetalleNominaController(SceneManager sceneManager,
                                   SesionManager sesion,
                                   StaffService staffService) {

        this.sceneManager = sceneManager;
        this.sesion = sesion;
        this.staffService = staffService;
    }

    @FXML
    public void initialize() {

        Nomina nomina = sesion.getNominaSeleccionada();

        if (nomina == null) return;

        Usuario usuario = staffService.listarEmpleados().stream()
                .filter(u -> u.getIdUsuario() == nomina.getIdUsuario())
                .findFirst()
                .orElse(null);

        lblNombre.setText(usuario != null ? usuario.getNombre() : "Desconocido");
        lblRol.setText("Trabajador");
        lblHoras.setText(String.valueOf(nomina.getHorasTrabajadas()));
        lblExtra.setText(String.valueOf(nomina.getHorasExtra()));
        lblTarifa.setText("$" + nomina.getTarifaPorHora());
        lblTotal.setText("$" + nomina.getTotal());
        lblEstado.setText(nomina.isPagado() ? "Pagado" : "Pendiente");
    }

    @FXML
    void On_volver() {

        try {
            sceneManager.showNomina();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
