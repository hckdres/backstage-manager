package org.example.ax0006.Service;

import org.example.ax0006.Repository.InventarioObjetoRepository;

public class InventarioObjetoService {
    private InventarioObjetoRepository invObjetoRepo;

    public InventarioObjetoService(InventarioObjetoRepository invObjetoRepo) {
        this.invObjetoRepo = invObjetoRepo;
    }

    public int vincularObjetoAInventario(int idInventario, int idObjeto) {
        return invObjetoRepo.guardarObjetoEnInventario(idInventario, idObjeto);
    }
}