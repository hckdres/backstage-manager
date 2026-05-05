package org.example.ax0006.Entity;

public class AnalisisFinanciero {

    private int idAnalisisF;
    private int presupuesto;
    private int gastos;
    private boolean aprobado;
    private int precioBoleta;

    public AnalisisFinanciero() {}

    public AnalisisFinanciero(int presupuesto, int gastos, boolean aprobado, int precioBoleta) {
        this.presupuesto = presupuesto;
        this.gastos = gastos;
        this.aprobado = aprobado;
        this.precioBoleta = precioBoleta;
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

    public int getPrecioBoleta(){
        return precioBoleta;
    }

    public void setPrecioBoleta(int precioBoleta){
        this.precioBoleta = precioBoleta;
    }

    public boolean getAprobado(){
        return aprobado;
    }

    public void setAprobado(boolean aprobado){
        this.aprobado = aprobado;
    }

}