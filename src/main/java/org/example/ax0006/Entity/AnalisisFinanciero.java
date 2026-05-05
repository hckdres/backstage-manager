package org.example.ax0006.Entity;

public class AnalisisFinanciero {

    private int idAnalisisF;
    private int presupuesto;
    private int gastos;

    public AnalisisFinanciero() {}

    public AnalisisFinanciero(int presupuesto, int gastos) {
        this.presupuesto = presupuesto;
        this.gastos = gastos;
    }

    public int getIdAnalisisF() {
        return idAnalisisF;
    }

    public void setIdAnalisisF(int idAnalisisF) {
        this.idAnalisisF = idAnalisisF;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public int getGastos() {
        return gastos;
    }

    public void setGastos(int gastos) {
        this.gastos = gastos;
    }
}