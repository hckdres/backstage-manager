package org.example.ax0006.Service;

import org.example.ax0006.Entity.ModeloObjeto;
import org.example.ax0006.Entity.Objeto;
import org.example.ax0006.Entity.TipoObjeto;
import org.example.ax0006.Repository.*;

import java.util.List;

public class ObjetoService {

    private TipoObjetoRepository tipoRepo;
    private ModeloObjetoRepository modeloRepo;
    private ObjetoRepository objetoRepo;

    public ObjetoService(TipoObjetoRepository tipoRepo,
                         ModeloObjetoRepository modeloRepo,
                         ObjetoRepository objetoRepo) {
        this.tipoRepo = tipoRepo;
        this.modeloRepo = modeloRepo;
        this.objetoRepo = objetoRepo;
    }

    // crear tipo
    public int crearTipo(String nombre) {
        return tipoRepo.guardar(nombre);
    }

    // crear modelo
    public int crearModelo(String nombre, int idTipo) {
        return modeloRepo.guardar(nombre, idTipo);
    }

    // crear objeto físico
    public void crearObjeto(int idModelo, int idInventario, String estado, String obs) {
        objetoRepo.guardar(idModelo, idInventario, estado, obs, true);
    }

    public List<Objeto> obtenerObjetosInventario(int idInventario){
        return objetoRepo.obtenerObjetosInventario(idInventario);
    }

    public void eliminarObjeto(int idObjeto){
        objetoRepo.eliminar(idObjeto);
    }


    // 🔹 tipos
    public List<TipoObjeto> obtenerTipos() {
        return tipoRepo.obtenerTodos();
    }

    // 🔹 modelos por tipo
    public List<ModeloObjeto> obtenerModelosPorTipo(int idTipo) {
        return modeloRepo.obtenerPorTipo(idTipo);
    }

    // 🔹 crear objeto
    public void crearObjeto(int idModelo, int idInventario,
                            String estado, String obs, boolean dis) {

        objetoRepo.guardar(idModelo, idInventario, estado, obs, dis);
    }
}