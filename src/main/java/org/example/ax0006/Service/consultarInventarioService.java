package org.example.ax0006.Service;

import org.example.ax0006.Entity.Inventario;
import org.example.ax0006.Repository.InventarioRepository;

public class consultarInventarioService {

    private InventarioRepository inventarioRepository;

    public consultarInventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    public Inventario consultarPorId(int id) {
        return inventarioRepository.buscarInventarioPorId(id);
    }

    public String obtenerDetalle(int id) {
        return inventarioRepository.obtenerDetalleInventario(id);
    }
}