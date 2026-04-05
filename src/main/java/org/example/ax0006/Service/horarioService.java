package org.example.ax0006.Service;

import org.example.ax0006.Repository.horarioRepository;

public class horarioService {

    private horarioRepository horariorepository;

    public horarioService(horarioRepository horariorepository) {
        this.horariorepository = horariorepository;
    }

    public int crearHorario(String fecha, String horaInicio, String horaFin) {

        if (fecha.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty()) {
            return -1;
        }

        return horariorepository.guardarHorario(fecha, horaInicio, horaFin);
    }
}