package org.example.ax0006.Service;

import org.example.ax0006.Entity.Horario;
import org.example.ax0006.Repository.InventarioObjetoRepository;

public class InventarioObjetoService {
    private final InventarioObjetoRepository inventarioObjetoRepository;

    public InventarioObjetoService(InventarioObjetoRepository inventarioObjetoRepository) {
        this.inventarioObjetoRepository = inventarioObjetoRepository;
    }

    public int guardarObjetoEnInventario(int inventarioId, int objetoId, Horario hNuevo) {
        return inventarioObjetoRepository.guardarObjetoEnInventario(inventarioId, objetoId, hNuevo);
    }

    public boolean objetoEnUsoEnRango(int objetoId, Horario hNuevo) {
        return inventarioObjetoRepository.objetoEnUsoEnRango(objetoId, hNuevo);
    }
}