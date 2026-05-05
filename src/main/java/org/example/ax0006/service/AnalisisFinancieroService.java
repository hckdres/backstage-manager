package org.example.ax0006.service;

import org.example.ax0006.entity.AnalisisFinanciero;
import org.example.ax0006.repository.AnalisisFinancieroRepository;

public class AnalisisFinancieroService {

    private AnalisisFinancieroRepository repo;

    public AnalisisFinancieroService(AnalisisFinancieroRepository repo) {
        this.repo = repo;
    }

    // =========================
    // CREAR PRESUPUESTO
    // =========================
    public int crearPresupuesto(int monto) {

        if (monto <= 0) return 0;

        AnalisisFinanciero af = new AnalisisFinanciero();
        af.setPresupuesto(monto);
        af.setGastos(0);
        af.setAprobado(false);
        af.setPrecioBoleta(0);

        return repo.guardar(af);
    }

    // =========================
    // ELIMINAR PRESUPUESTO
    // =========================
    public void eliminarPresupuesto(int id) {
        repo.eliminar(id);
    }

    // =========================
    // REGISTRAR GASTO
    // =========================
    public void registrarGasto(int id, int gasto) {

        if (gasto <= 0) return;

        AnalisisFinanciero af = repo.buscarPorId(id);
        if (af == null) return;

        int nuevoTotal = af.getGastos() + gasto;

        repo.actualizarGastos(id, nuevoTotal);
    }

    // =========================
    // ELIMINAR GASTO
    // =========================
    public void eliminarGasto(int id, int gasto) {

        if (gasto <= 0) return;

        AnalisisFinanciero af = repo.buscarPorId(id);
        if (af == null) return;

        int nuevoTotal = af.getGastos() - gasto;
        if (nuevoTotal < 0) nuevoTotal = 0;

        repo.actualizarGastos(id, nuevoTotal);
    }

    // =========================
    // APROBAR PRESUPUESTO
    // =========================
    public void aprobarPresupuesto(int id) {

        AnalisisFinanciero af = repo.buscarPorId(id);
        if (af == null) return;

        // Regla de negocio
        if (af.getGastos() > af.getPresupuesto()) {
            return; // no se aprueba si hay sobrecosto
        }

        repo.aprobar(id);
    }

    // =========================
    // DEFINIR PRECIO BOLETA
    // =========================
    public void definirPrecioBoleta(int id, int precio) {

        if (precio <= 0) return;

        repo.actualizarPrecioBoleta(id, precio);
    }

    // =========================
    // CONSULTAR BALANCE
    // =========================
    public int obtenerBalance(int id) {

        AnalisisFinanciero af = repo.buscarPorId(id);
        if (af == null) return 0;

        return af.getPresupuesto() - af.getGastos();
    }
}