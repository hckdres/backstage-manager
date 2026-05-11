package org.example.ax0006.service;

import org.example.ax0006.entity.Concierto;
import org.example.ax0006.entity.Contrato;
import org.example.ax0006.repository.ConciertoRepository;
import org.example.ax0006.repository.HorarioRepository;
import org.example.ax0006.validator.ConciertoValidator;

import java.util.ArrayList;
import java.util.List;

public class ConciertoService {

    private ConciertoRepository conciertoRepo;
    private HorarioRepository horarioRepo;
    private ConciertoValidator conciertoValidator;
    private ContratoService contratoService;
    private InventarioService inventarioService;

    public ConciertoService(ConciertoRepository conciertoRepo, HorarioRepository horarioRepo,
                            ConciertoValidator conciertoValidator, ContratoService contratoService,
                            InventarioService inventarioService) {
        this.conciertoRepo = conciertoRepo;
        this.horarioRepo = horarioRepo;
        this.conciertoValidator = conciertoValidator;
        this.contratoService = contratoService;
        this.inventarioService = inventarioService;
    }

    public void crearConcierto(Concierto c) {
        Contrato contrato = contratoService.obtenerContratoCompleto(c.getIdContrato());
        conciertoValidator.validar(c, contrato);

        int idHorario = horarioRepo.guardar(c.getHorario());
        int idConcierto = conciertoRepo.guardar(c, idHorario);
        conciertoRepo.guardarRelacionArtista(c.getArtista().getIdUsuario(), idConcierto, 3);

        inventarioService.crearDocumentoInventario(idConcierto, idHorario, new ArrayList<>());
    }

    public List<Concierto> obtenerConciertosSolos() {
        return conciertoRepo.obtenerConciertosSolos();
    }

    public List<Concierto> obtenerConciertos() {
        return conciertoRepo.obtenerConciertos();
    }

    public void aprobarConcierto(int idConcierto) {
        conciertoRepo.aprobarConcierto(idConcierto);
    }

    public void eliminarConcierto(int idConcierto, int idHorario) {
        conciertoRepo.eliminarConcierto(idConcierto);
        horarioRepo.eliminarHorario(idHorario);
    }

    public void eliminarInventarioConcierto(int idInventario, int idConcierto, int idHorario, List<Integer> idsObjetos){
        inventarioService.eliminarDocumentoInventario(idInventario, idConcierto, idHorario, idsObjetos);
    }


    public List<Concierto> listarConciertos() {
        return conciertoRepo.obtenerConciertos();
    }
}