package org.example.ax0006.Service;

import org.example.ax0006.Repository.InventarioRepository;
import java.util.List;

public class InventarioService {
    private InventarioRepository inventarioRepo;

    public InventarioService(InventarioRepository inventarioRepo) {
        this.inventarioRepo = inventarioRepo;
    }

    public int crearDocumentoInventario(int idConcierto, int idHorario, List<Integer> idsObjetos) {
        return inventarioRepo.crearDocumentoInventario(idConcierto, idHorario, idsObjetos);
    }

    public List<String> obtenerObjetosPorConcierto(int idConcierto) {
        return inventarioRepo.obtenerObjetosPorConcierto(idConcierto);
    }

}