package org.example.ax0006.Service;

import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.Repository.TipoObjetoRepository;

public class crearTipoObjetoService {
    private TipoObjetoRepository tipoObjetoRepository;

    public crearTipoObjetoService(TipoObjetoRepository tipoObjetoRepository) {
        this.tipoObjetoRepository = tipoObjetoRepository;
    }

    public boolean crearTipoObjeto(String nombre) {
        if (tipoObjetoRepository.buscarPorNombreTipoObjeto(nombre) != null) {
            System.out.println("el objeto ya existe");
            return false;
        }

        TipoObjeto tipoObjeto = new TipoObjeto(nombre);
        tipoObjetoRepository.guardarTipoObjeto(tipoObjeto);
        return true;

    }
}
